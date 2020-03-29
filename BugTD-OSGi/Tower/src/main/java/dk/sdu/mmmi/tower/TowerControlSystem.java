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
import dk.sdu.mmmi.commontower.Tower;
import java.util.ArrayList;
import java.util.List;

public class TowerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        // Creating new Tower with ClickEvent
        List<Event> eventsToDelete = new ArrayList<>();
        for (Event event : gameData.getEvents()) {
            if (!(event instanceof ClickEvent)) {
                continue;
            }
            
            int clickX = ((ClickEvent) event).getX();
            int clickY = gameData.getDisplayHeight() - ((ClickEvent) event).getY();  // The y-value needs to be reversed for unknown reason
            
            
            createNewTower(world, clickX, clickY);
        }
        gameData.getEvents().removeAll(eventsToDelete);

        // Processing existing Towers
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
    
    private void createNewTower(World world, int xpos, int ypos) {
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

        Tower tower = new Tower(pos, life, colli, wpn, sprt);
        world.addEntity(tower);
    }
}
