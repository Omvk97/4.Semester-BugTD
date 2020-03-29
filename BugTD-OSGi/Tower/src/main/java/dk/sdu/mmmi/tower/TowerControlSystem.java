package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.TileSizes;
import dk.sdu.mmmi.commontower.Tower;
import java.util.ArrayList;
import java.util.List;

public class TowerControlSystem implements IEntityProcessingService {

    private MapSPI map;

    @Override
    public void process(GameData gameData, World world) {
        
        createNewTowers(gameData, world);
        
        for (Entity tower : world.getEntities(Tower.class)) {
            PositionPart towerPosPart = tower.getPart(PositionPart.class);
            Entity target = calculateClosestEnemy(world, towerPosPart);      // Or something
            if (target != null) {
                WeaponPart weapon = tower.getPart(WeaponPart.class);
                weapon.setTarget(target);
                if (distance(towerPosPart, target.getPart(PositionPart.class)) < weapon.getRange()) {
                    weapon.process(gameData, target);   // Dont really know what to use as arguments   
                }
            }
        }
    }
    
    private void createNewTowers(GameData gameData, World world) {
        List<Event> eventsToDelete = new ArrayList<>();
        for (Event event : gameData.getEvents()) {
            if (!(event instanceof ClickEvent)) {
                continue;
            }
            eventsToDelete.add(event);

            // Calculate placement of new Tower
            int clickX = ((ClickEvent) event).getX();
            int clickY = gameData.getDisplayHeight() - ((ClickEvent) event).getY();  // The y-value needs to be reversed for unknown reason
            clickX = roundDown(clickX, TileSizes.GRASS_WIDTH);
            clickY = roundDown(clickY, TileSizes.GRASS_WIDTH);
            Tower tower = createNewTower(world, clickX, clickY);

            // Retrieve the Tiles on which the new Tower shall be placed
            Tile[] tiles = new Tile[4];
            int numberFromLeft = clickX / TileSizes.GRASS_WIDTH;
            int numberFromBottom = clickY / TileSizes.GRASS_WIDTH;
            tiles[0] = map.getTiles()[numberFromLeft][numberFromBottom];
            tiles[1] = map.getTiles()[numberFromLeft + 1][numberFromBottom];
            tiles[2] = map.getTiles()[numberFromLeft][numberFromBottom + 1];
            tiles[3] = map.getTiles()[numberFromLeft + 1][numberFromBottom + 1];

            // Check each Tile for its availability
            boolean illegalTowerPlacement = false;
            for (Tile tile : tiles) {
                if (tile.getOccupant() != null) {
                    illegalTowerPlacement = true;
                    break;
                }
            }

            //Assign Tower as occupant for each Tile
            if (!illegalTowerPlacement) {
                for (Tile tile : tiles) {
                    tile.setOccupant(tower);
                }
                world.addEntity(tower);
            }
        }
        gameData.getEvents().removeAll(eventsToDelete);
    }

    int roundDown(double number, double place) {
        double result = number / place;
        result = Math.floor(result);
        result *= place;
        return (int) result;
    }

    private Entity calculateClosestEnemy(World world, PositionPart towerPosPart) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestEnemy = null;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart enemyPosPart = enemy.getPart(PositionPart.class);
            float distance = distance(towerPosPart, enemyPosPart);
            if (distance < currentMinDistance) {
                currentMinDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }

    private Entity calculateLowestHealthEnemy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private float distance(PositionPart towerPosPart, PositionPart enemyPosPart) {
        float dx = (float) towerPosPart.getX() - (float) enemyPosPart.getX();
        float dy = (float) towerPosPart.getY() - (float) enemyPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private Tower createNewTower(World world, int xpos, int ypos) {
        float x = xpos;
        float y = ypos;
        float radians = 0;
        PositionPart pos = new PositionPart(x, y, radians);

        int hp = 100;
        LifePart life = new LifePart(hp);

        CollisionPart colli = new CollisionPart();

        float damage = 10;
        float range = 50;
        float speed = 1;
        WeaponPart wpn = new WeaponPart(damage, range, speed);

        int width = 32;
        int height = 32;
        int layer = 1;
        SpritePart sprt = new SpritePart("basictower.png", width, height, layer);

        return new Tower(pos, life, colli, wpn, sprt);
    }

    public void setMapSPI(MapSPI spi) {
        this.map = spi;
    }
}
