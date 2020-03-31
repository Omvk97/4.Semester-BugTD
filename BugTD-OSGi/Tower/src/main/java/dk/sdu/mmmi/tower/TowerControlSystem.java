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
import dk.sdu.mmmi.commontower.TowerPreview;
import java.util.ArrayList;
import java.util.List;

public class TowerControlSystem implements IEntityProcessingService {

    private MapSPI map;
    private TowerPreview preview;

    @Override
    public void process(GameData gameData, World world) {
        showTowerPlacementPreview(gameData, world);
        createNewTowers(gameData, world);
        attackEnemies(gameData, world);
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
            int clickY = ((ClickEvent) event).getY();
            clickX = roundDown(clickX, TileSizes.GRASS_WIDTH);
            clickY = roundDown(clickY, TileSizes.GRASS_WIDTH);
            Tower tower = createNewTower(clickX, clickY);

            //Assign Tower as occupant for each Tile
            Tile[] tiles = new Tile[4];
            if (isLegalPlacement(tiles, clickX, clickY)) {
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

    private boolean isLegalPlacement(Tile[] tiles, int x, int y) {
        int numberFromLeft = x / TileSizes.GRASS_WIDTH;
        int numberFromBottom = y / TileSizes.GRASS_WIDTH;
        try {
            tiles[0] = map.getTiles()[numberFromLeft][numberFromBottom];
            tiles[1] = map.getTiles()[numberFromLeft + 1][numberFromBottom];
            tiles[2] = map.getTiles()[numberFromLeft][numberFromBottom + 1];
            tiles[3] = map.getTiles()[numberFromLeft + 1][numberFromBottom + 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;   // Return illegal as the placement is out of bounds
        }

        // Check each Tile for its availability
        for (Tile tile : tiles) {
            if (tile.getOccupant() != null || !tile.isWalkable()) {
                return false;
            }
        }
        return true;
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

    private Tower createNewTower(int xpos, int ypos) {
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
        SpritePart sprt = new SpritePart(SingleDamageTowerPlugin.BASIC_TOWER_PATH, width, height, layer);

        return new Tower(pos, life, colli, wpn, sprt);
    }

    public void setMapSPI(MapSPI spi) {
        this.map = spi;
    }

    private void showTowerPlacementPreview(GameData gameData, World world) {
        // Create the preview entity for the first time
        if (preview == null) {
            TowerPreview towerPreview = new TowerPreview(
                    new PositionPart(0, 0, 0),
                    new SpritePart(SingleDamageTowerPlugin.BASIC_TOWER_PREVIEW_LEGAL_PATH, 32, 32, 2)
            );
            world.addEntity(towerPreview);
            preview = towerPreview;
        }

        // Calculate placement of preview
        PositionPart posPart = preview.getPart(PositionPart.class);
        int x = roundDown(gameData.getMouseX(), TileSizes.GRASS_WIDTH);
        int y = roundDown(gameData.getMouseY(), TileSizes.GRASS_WIDTH);
        posPart.setX(x);
        posPart.setY(y);

        // Set preview sprite according to legalness of placement
        Tile[] tiles = new Tile[4];
        String spritePath;
        if (isLegalPlacement(tiles, x, y)) {
            spritePath = SingleDamageTowerPlugin.BASIC_TOWER_PREVIEW_LEGAL_PATH;
        } else {
            spritePath = SingleDamageTowerPlugin.BASIC_TOWER_PREVIEW_ILLEGAL_PATH;
        }
        preview.remove(SpritePart.class);
        preview.add(new SpritePart(spritePath, 32, 32, 2, 75));
    }

    private void attackEnemies(GameData gameData, World world) {
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
}
