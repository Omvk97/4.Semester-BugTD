package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.*;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.events.EnemyDiedEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commontower.Tower;

public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart p = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            AnimationPart animationPart = enemy.getPart(AnimationPart.class);
            Entity target = calculateClosestTower(world, p);
            if (target != null) {
                WeaponPart weapon = enemy.getPart(WeaponPart.class);
                weapon.setTarget(target);
                if (distance(p, target.getPart(PositionPart.class)) < weapon.getRange()) {
                    weapon.process(gameData, target);   // Dont really know what to use as arguments   
                }
            }

            moveEnemy(enemy, p, movingPart, animationPart);

            // System.out.println("Enemy X = " + p.getX() + " Y = " + p.getY());
            setAnimation(enemy, gameData, animationPart);

            movingPart.process(gameData, enemy);
            p.process(gameData, enemy);

            if (((LifePart) enemy.getPart(LifePart.class)).isDead()) {
                gameData.addEvent(new EnemyDiedEvent(enemy));
                world.removeEntity(enemy);
            }
        }
    }

    public void moveEnemy(Entity enemy, PositionPart pos, MovingPart move, AnimationPart animPart) {
        float targetX = 600;
        float targetY = 300;
        move.setRight(false);
        move.setLeft(false);
        move.setDown(false);
        move.setUp(false);
        if (pos.getX() != targetX) {
            if (pos.getX() - 5 < targetX) {
                move.setRight(true);
                animPart.setAtlasPath("texturesprites/enemy/enemyright.atlas");

            }
            if (pos.getX() + 5 > targetX) {
                move.setLeft(true);
                animPart.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
            }

            if (pos.getY() + 5 > targetY) {
                move.setDown(true);
                animPart.setAtlasPath("texturesprites/enemy/enemydown.atlas");
            }
            if (pos.getY() - 5 < targetY) {
                move.setUp(true);
                animPart.setAtlasPath("texturesprites/enemy/enemyup.atlas");
            }
        }

    }

    public void setAnimation(Entity enemy, GameData gameData, AnimationPart animPart) {
        if (gameData.getKeys().isDown(DOWN)) {
            animPart.setAtlasPath("texturesprites/enemy/enemydown.atlas");
        }
        if (gameData.getKeys().isDown(UP)) {
            animPart.setAtlasPath("texturesprites/enemy/enemyup.atlas");
        }
        if (gameData.getKeys().isDown(RIGHT)) {
            animPart.setAtlasPath("texturesprites/enemy/enemyright.atlas");
        }
        if (gameData.getKeys().isDown(LEFT)) {
            animPart.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
        }
    }

    private Entity calculateClosestTower(World world, PositionPart towerPosPart) {
        float currentMinDistance = Float.MAX_VALUE;
        Entity closestEnemy = null;

        for (Entity enemy : world.getEntities(Tower.class)) {
            PositionPart enemyPosPart = enemy.getPart(PositionPart.class);
            float distance = distance(towerPosPart, enemyPosPart);
            if (distance < currentMinDistance) {
                currentMinDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }

    private Entity calculateLowestHealthTower() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private float distance(PositionPart enemyPosPart, PositionPart towerPosPart) {
        float dx = (float) enemyPosPart.getX() - (float) towerPosPart.getX();
        float dy = (float) enemyPosPart.getY() - (float) towerPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
