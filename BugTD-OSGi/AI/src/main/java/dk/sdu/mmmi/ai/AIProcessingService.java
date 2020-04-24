/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.ai.astar.TileRouteFinder;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonai.events.MapChangedDuringRoundEvent;
import dk.sdu.mmmi.commonai.events.RouteCalculatedEvent;
import dk.sdu.mmmi.commonenemy.Command;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyCommand;
import dk.sdu.mmmi.commonenemy.EnemyType;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commontower.Tower;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author oliver
 */
public class AIProcessingService implements IEntityProcessingService {

    private MapSPI mapSPI;
    private TileRouteFinder routeFinder = null;

    @Override
    public void process(GameData gameData, World world) {
        // Start up initialization
        if (routeFinder == null) {
            routeFinder = TileRouteFinder.getInstance(mapSPI);
        }

        Set<Enemy> enemiesToCalculate = new HashSet<>(); // Set to avoid duplicates if enemy is both spwaning and tower is placed at the same time
        // Enemy Spawned event listener
        gameData.getEvents(EnemySpawnedEvent.class).forEach((event) -> {
            EnemySpawnedEvent enemySpawnedEvent = (EnemySpawnedEvent) event;
            enemiesToCalculate.add(enemySpawnedEvent.getEnemy());
            gameData.removeEvent(event);
        });

        boolean mapHasChanged = false;
        // Map changed event listener which should trigger re calibration of all enemies and re calibration of all connections between tiles
        if (!(gameData.getEvents(MapChangedDuringRoundEvent.class)).isEmpty()) {
            mapHasChanged = true;
            world.getEntities(Enemy.class).forEach((enemy) -> {
                enemiesToCalculate.add((Enemy) enemy);
            });
            gameData.removeEvents(MapChangedDuringRoundEvent.class);
        }

        // Calculate best route for enemy
        Tile[][] tiles = mapSPI.getTiles();
        Tile queenTile = mapSPI.getTiles()[40][33];

        for (Enemy enemy : enemiesToCalculate) {
            try {
                // Calculate best route for enemy
                List<Tile> tileRoute;

                Tile startTile = mapSPI.getTilesEntityIsOn(enemy).get(0);

                if (enemy.getType() == EnemyType.GROUND) {
                    tileRoute = routeFinder.findBestRouteForGroundEnemy(tiles, startTile, queenTile, mapHasChanged);

                    List<EnemyCommand> enemyCommands = new ArrayList<>();
                    // if there is no route, a tower must be destroyed, which will trigger a recalibration of AI for all enemies
                    if (tileRoute.isEmpty()) {
                        enemyCommands.add(new EnemyCommand(calculateClosestTower(world, enemy.getCurrentX(), enemy.getCurrentY()), Command.ATTACK));
                    } else {
                        tileRoute.forEach((tile) -> {
                            enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                        });
                    }

                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                } else if (enemy.getType() == EnemyType.FLYING) {
                    tileRoute = routeFinder.findBestRouteForFlyingEnemy(tiles, startTile, queenTile);

                    List<EnemyCommand> enemyCommands = new ArrayList<>();
                    tileRoute.forEach((tile) -> {
                        enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                    });

                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                }
                if (enemy.getType() == EnemyType.ATTACKING) {
                    List<EnemyCommand> enemyCommands = new ArrayList<>();

                    enemyCommands.add(new EnemyCommand(calculateClosestTower(world, enemy.getCurrentX(), enemy.getCurrentY()), Command.ATTACK));

                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                }
                // ensure that if map has changed at some point, connections is only calculated the first time.
                mapHasChanged = false;

            } catch (IllegalStateException ex) {
                // No route found, therefore attack closest tower
                List<EnemyCommand> enemyCommands = new ArrayList<>();

                enemyCommands.add(new EnemyCommand(calculateClosestTower(world, enemy.getCurrentX(), enemy.getCurrentY()), Command.ATTACK));

                gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
            }
        }
    }

    private Tower calculateClosestTower(World world, float xPosition, float yPosition) {
        Tower closestTower = null;
        double closestDistance = 0;
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Tower) {
                PositionPart towerPosition = entity.getPart(PositionPart.class);
                double distance = Math.sqrt(Math.sqrt(xPosition - towerPosition.getX()) + Math.sqrt(yPosition - towerPosition.getY()));
                if (closestTower == null) {
                    closestTower = (Tower) entity;
                    closestDistance = distance;
                } else {
                    if (distance < closestDistance) {
                        closestTower = (Tower) entity;
                        closestDistance = distance;
                    }
                }
            }
        }
        return closestTower;
    }

    public MapSPI getMapSPI() {
        return mapSPI;
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
    }
}
