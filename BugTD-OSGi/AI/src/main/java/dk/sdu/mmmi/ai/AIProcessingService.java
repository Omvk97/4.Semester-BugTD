package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.ai.astar.TileRouteFinder;
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
import dk.sdu.mmmi.commonai.events.MapChangedDuringRoundEvent;
import dk.sdu.mmmi.commonai.events.RouteCalculatedEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyType;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commontower.Tower;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AIProcessingService extends EventObserver implements IEntityProcessingService, AIProcessingServiceSPI {
    
    private boolean mapHasChanged;
    private final List<Entity> changedTowers = new ArrayList<>();
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
        TileRouteFinder routeFinder = AIPlugin.getRouteFinder();
        if (mapSPI == null) {
            return;
        }
        // Start up initialization
        if (routeFinder == null) {
            routeFinder = TileRouteFinder.getInstance(mapSPI);
        }

        if (AIPlugin.isNewGame()) {
            mapHasChanged = true;
            AIPlugin.setNewGame(false);
        }
        
//        // Map changed event listener which should trigger re calibration of all enemies and re calibration of all connections between tiles
//        for (Event mapChangedEvent : gameData.getEvents(MapChangedDuringRoundEvent.class)) {
//            changedTowers.add(mapChangedEvent.getSource());
//            gameData.removeEvents(MapChangedDuringRoundEvent.class);
//        }

        if (!changedTowers.isEmpty()) {
            mapHasChanged = true;
            world.getEntities(Enemy.class).forEach((enemy) -> {
                enemiesToCalculate.add((Enemy) enemy);
            });
        }

        // Calculate best route for enemy
        Tile[][] tiles = mapSPI.getTiles();
        Tile queenTile = mapSPI.getTileInDirection(mapSPI.getTilesEntityIsOn(mapSPI.getQueen()).get(0), Direction.LEFT);
        Iterator<Enemy> it = enemiesToCalculate.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            Tile startTile = mapSPI.getTilesEntityIsOn(enemy).get(0);
            try {
                // Calculate best route for enemy
                List<Tile> tileRoute;

                if (enemy.getType() == EnemyType.GROUND) {

                    tileRoute = routeFinder.findBestRouteForGroundEnemy(tiles, startTile, queenTile, mapHasChanged, changedTowers);

                    List<EnemyCommand> enemyCommands = new ArrayList<>();

                    tileRoute.forEach((tile) -> {
                        enemyCommands.add(new EnemyCommand(tile, Command.WALK));
                    });

                    enemyCommands.add(new EnemyCommand(mapSPI.getQueen(), Command.ATTACK));
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

                    enemyCommands.add(new EnemyCommand(calculateClosestTower(world, enemy.getPositionPart()), Command.ATTACK));

                    gameData.addEvent(new RouteCalculatedEvent(enemy, enemyCommands));
                }

            } catch (IllegalStateException ex) {     // No route found, therefore attack closest tower
                List<EnemyCommand> enemyCommands = new ArrayList<>();

                Entity target = calculateClosestTower(world, enemy.getPositionPart());

                if (target != null) {   // No towers check
                    Tile lowerLeft = mapSPI.getTilesEntityIsOn(target).get(0);
                    Tile upperLeft = mapSPI.getTilesEntityIsOn(target).get(1);
                    Tile lowerRight = mapSPI.getTilesEntityIsOn(target).get(2);
                    Tile upperRight = mapSPI.getTilesEntityIsOn(target).get(3);
                    
                    System.out.println("This is lower left x pos: " + lowerLeft.getX() + " this is lower left Y pos: " + lowerLeft.getY());
                    System.out.println("This is upper right x pos: " + upperRight.getX() + " this is upper right Y pos: " + upperRight.getY());
                    System.out.println("This is upper left x pos: " + upperLeft.getX() + " this is uppper left Y pos: " + upperLeft.getY());
                    System.out.println("This is lower right x pos: " + lowerRight.getX() + " this is lowerright Y pos: " + lowerRight.getY());
                    
                    float enemyX = enemy.getPositionPart().getX();
                    float enemyY = enemy.getPositionPart().getY();
                    float targetX = ((PositionPart) target.getPart(PositionPart.class)).getX();
                    float targetY = ((PositionPart) target.getPart(PositionPart.class)).getY();
                    int value = 0;
                    
                    if(enemyX > targetX) {
                        value += 2;
                        
                    }
                    if(enemyY > targetY) {
                        value += 1;
                    }
                    System.out.println("Value = " + value);
                    
                    
                    Tile towerTile = mapSPI.getTilesEntityIsOn(target).get(value);
                    System.out.println("chosen tile Pos: " + towerTile.getX() + " " + towerTile.getY());
                    List<Tile> tileRoute = routeFinder.findBestRouteForGroundEnemy(tiles, startTile, towerTile, true, new ArrayList(Arrays.asList(target)));

                    tileRoute.forEach((tile) -> {
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

    public Entity calculateClosestTower(World world, PositionPart enemyPosPart) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestTower = null;

        for (Entity tower : world.getEntities(Tower.class)) {
            PositionPart towerPosPart = tower.getPart(PositionPart.class);
            System.out.println("Tower pos: " + towerPosPart.getX() + " " + towerPosPart.getY());
            float distance = distance(enemyPosPart, towerPosPart);
            if (distance < currentMinDistance) {
                currentMinDistance = distance;
                closestTower = tower;
            }
        }
        System.out.println("closest tower pos :" + ((PositionPart) closestTower.getPart(PositionPart.class)).getX() + " " + ((PositionPart) closestTower.getPart(PositionPart.class)).getY());
        return closestTower;
    }

    public float distance(PositionPart enemyPosPart, PositionPart towerPosPart) {
        float dx = (float) enemyPosPart.getX() - (float) towerPosPart.getX();
        float dy = (float) enemyPosPart.getY() - (float) towerPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
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
                 changedTowers.add(e.getSource());
                break;
            default:
                break;
        }
    }
}
