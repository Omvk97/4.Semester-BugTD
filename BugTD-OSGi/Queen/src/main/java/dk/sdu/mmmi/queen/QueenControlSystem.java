package dk.sdu.mmmi.queen;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.events.GameOverEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commontower.Queen;

public class QueenControlSystem implements IEntityProcessingService {

    private Queen queen;
    private MapSPI map;

    @Override
    public void process(GameData gameData, World world) {
        if (queen == null || !world.getEntities(Queen.class).contains(queen)) {
            queen = (Queen) world.getEntities(Queen.class).get(0);
            map.fitEntityToMap(queen);
        }

        // Check if dead
        if (((LifePart) queen.getPart(LifePart.class)).isDead()) {
            gameData.addEvent(new GameOverEvent(queen));
        }

        // Attack enemies
        PositionPart queenPosPart = queen.getPart(PositionPart.class);
        Entity target = calculateClosestEnemy(world, queenPosPart);      // Or something
        if (target != null) {
            WeaponPart weapon = queen.getPart(WeaponPart.class);
            weapon.setTarget(target);
            if (distance(queenPosPart, target.getPart(PositionPart.class)) < weapon.getRange()) {
                weapon.process(gameData, target);   // Dont really know what to use as arguments   
            }
        }
    }

    private Entity calculateClosestEnemy(World world, PositionPart queenPosPart) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestEnemy = null;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart enemyPosPart = enemy.getPart(PositionPart.class);
            float distance = distance(queenPosPart, enemyPosPart);
            if (distance < currentMinDistance) {
                currentMinDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }

    private float distance(PositionPart towerPosPart, PositionPart enemyPosPart) {
        float dx = (float) towerPosPart.getX() - (float) enemyPosPart.getX();
        float dy = (float) towerPosPart.getY() - (float) enemyPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setMapSPI(MapSPI spi) {
        this.map = spi;
    }
}