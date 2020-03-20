package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commontower.Tower;

public class TowerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity tower : world.getEntities(Tower.class)) {
            PositionPart towerPosPart = tower.getPart(PositionPart.class);
            Entity target = calculateClosestEnemy(world, towerPosPart);      // Or something
            if (target != null) {
                WeaponPart weapon = tower.getPart(WeaponPart.class);
                weapon.setTarget(target);
                weapon.process(gameData, target);   // Dont really know what to use as arguments
            }
            updateShape(tower);
        }
    }

    private Entity calculateClosestEnemy(World world, PositionPart towerPosPart) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestEnemy = null;

//        for (Entity enemy : world.getEntities(Enemy.class)) {
//            PositionPart enemyPosPart = enemy.getPart(PositionPart.class);
//            float distance = distance(towerPosPart, enemyPosPart);
//            if (distance < currentMinDistance) {
//                currentMinDistance = distance;
//                closestEnemy = enemy;
//            }
//        }
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

    // To be replaced with some Sprite method
    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radius = entity.getRadius();

        shapex[0] = (float) (x - radius);
        shapey[0] = (float) (y - radius);

        shapex[1] = (float) (x - radius);
        shapey[1] = (float) (y + radius);

        shapex[2] = (float) (x + radius);
        shapey[2] = (float) (y + radius);

        shapex[3] = (float) (x + radius);
        shapey[3] = (float) (y - radius);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
