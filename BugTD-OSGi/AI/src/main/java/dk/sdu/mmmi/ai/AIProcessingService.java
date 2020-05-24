package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.ai.astar.scorers.QueenHeuristicScorer;
import dk.sdu.mmmi.ai.astar.RouteFinder;
import dk.sdu.mmmi.ai.astar.scorers.TilePathCostScorer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventObserver;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.AIProcessingServiceSPI;
import dk.sdu.mmmi.commonai.events.Command;
import dk.sdu.mmmi.commonai.events.EnemyCommand;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonai.events.RouteCalculatedEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyType;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commontower.Tower;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AIProcessingService extends EventObserver implements IEntityProcessingService, AIProcessingServiceSPI {

    private boolean mapHasChanged;
    private boolean firstTimeRunning = true;
    Set<Enemy> enemiesToCalculate = new HashSet<>(); // Set to avoid duplicates if enemy is both spwaning and tower is placed at the same time

    @Override
    public void process(GameData gameData, World world) {

        if (firstTimeRunning) {
            this.setRemoveEvent(true); // Will make sure that after an event has been observed that it will automatically be removed and therefore not saved
            gameData.listenForEvent(this, EventType.EnemySpawnedEvent, EventType.MapChangedDuringRoundEvent);
            firstTimeRunning = false;
        }

        MapSPI mapSPI = AIPlugin.getMapSPI();
        if (mapSPI == null) {
            return;
        }

        // Start up initialization
        if (AIPlugin.isNewGame()) {
            mapHasChanged = true;
            AIPlugin.setNewGame(false);
        }

        if (mapHasChanged) {
            mapHasChanged = false;
            world.getEntities(Enemy.class).forEach((enemy) -> {
                enemiesToCalculate.add((Enemy) enemy);
            });
        }

        // Calculate best route for enemy
        Tile queenTile = mapSPI.getTileInDirection(mapSPI.getTilesEntityIsOn(mapSPI.getQueen()).get(0), Direction.LEFT);
        Iterator<Enemy> it = enemiesToCalculate.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            Tile startTile = mapSPI.getTilesEntityIsOn(enemy).get(0);
            try {
                // Calculate best route for enemy
                if (enemy.getType() == EnemyType.GROUND) {
                    RouteFinder routeFinder = new RouteFinder(new TilePathCostScorer(), new QueenHeuristicScorer());
                    List<Tile> route = routeFinder.findRoute(startTile, queenTile, mapSPI);
                    List<EnemyCommand> enemyCommands = new ArrayList<>();
                    route.forEach((tile) -> {
                        enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                    });
                    enemyCommands.add(new EnemyCommand(mapSPI.getQueen(), Command.ATTACK));
                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));

                } else if (enemy.getType() == EnemyType.FLYING) {
                    // To make this route geenration diffrent from the ground enemies, we just have to make a new Scorer that ignores obstacles
                    RouteFinder realRouteFinder = new RouteFinder(new TilePathCostScorer(), new QueenHeuristicScorer());
                    List<Tile> route = realRouteFinder.findRoute(startTile, queenTile, mapSPI);

                    List<EnemyCommand> enemyCommands = new ArrayList<>();
                    route.forEach((tile) -> {
                        enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                    });
                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                }
                if (enemy.getType() == EnemyType.ATTACKING) {
                    List<EnemyCommand> enemyCommands = new ArrayList<>();
                    enemyCommands.add(new EnemyCommand(calculateClosestTower(world, enemy), Command.ATTACK));
                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                }

            } catch (IllegalStateException ex) {     // No route found, therefore attack closest tower
                List<EnemyCommand> enemyCommands = new ArrayList<>();

                Entity target = calculateClosestTower(world, enemy);

                if (target != null) {   // No towers check
                    float enemyX = enemy.getPositionPart().getX();
                    float enemyY = enemy.getPositionPart().getY();
                    float targetX = ((PositionPart) target.getPart(PositionPart.class)).getX();
                    float targetY = ((PositionPart) target.getPart(PositionPart.class)).getY();

                    int tileIndex = 0;
                    if (enemyX > targetX) {
                        tileIndex += 2;
                    }
                    if (enemyY > targetY) {
                        tileIndex += 1;
                    }
                    Tile towerTile = mapSPI.getTilesEntityIsOn(target).get(tileIndex);

                    RouteFinder routeFinder = new RouteFinder(new TilePathCostScorer(), new QueenHeuristicScorer());
                    List<Tile> route = routeFinder.findRoute(startTile, towerTile, mapSPI);

                    route.forEach((tile) -> {
                        enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                    });

                    enemyCommands.add(new EnemyCommand(target, Command.ATTACK));

                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));

                }
            } finally {
                // Ensure that if map has changed at some point, connections is only calculated the first time.
                mapHasChanged = false;
                it.remove();
            }
        }

    }

    @Override
    public Entity calculateClosestTower(World world, Entity enemy) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestTower = null;

        for (Entity tower : world.getEntities(Tower.class)) {
            float distance = AIPlugin.getMapSPI().distance(enemy, tower);
            if (distance < currentMinDistance) {
                currentMinDistance = distance;
                closestTower = tower;
            }
        }
        return closestTower;
    }

    public void setMapSPI(MapSPI mapSPI) {
        AIPlugin.setMapSPI(mapSPI);
    }

    public void removeMapSPI(MapSPI map) {
        AIPlugin.setMapSPI(null);
    }

    @Override
    public void methodToCall(Event e) {
        switch (e.getType()) {
            case EnemySpawnedEvent:
                EnemySpawnedEvent enemySpawnedEvent = (EnemySpawnedEvent) e;
                enemiesToCalculate.add((Enemy) enemySpawnedEvent.getEnemy());
                break;
            case MapChangedDuringRoundEvent:
                mapHasChanged = true;
                break;
            default:
                break;
        }
    }
}
