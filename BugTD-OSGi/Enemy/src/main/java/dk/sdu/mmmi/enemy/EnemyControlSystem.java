package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovementInstruction;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonenemy.EnemyType;
import dk.sdu.mmmi.commonai.events.RouteCalculatedEvent;
import dk.sdu.mmmi.commonenemy.Command;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyCommand;
import dk.sdu.mmmi.commontower.Tower;

public class EnemyControlSystem implements IEntityProcessingService {

    private int counter = 0;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            // TODO - place in enemy spawner
            if (counter == 0 || counter == 1) {
                if (enemy.getType() == EnemyType.GROUND) {
                    gameData.addEvent(new EnemySpawnedEvent((Enemy) enemy));
                }
                counter++;
            }

            if (((LifePart) enemy.getPart(LifePart.class)).isDead()) {
                world.removeEntity(enemy);
            }

            // AI Processing
            PositionPart positionPart = enemy.getPositionPart();
            PreciseMovingPart movingPart = enemy.getMovingPart();

//            Entity target = calculateClosestTower(world, p);
//            if (target != null) {
//                WeaponPart weapon = enemy.getPart(WeaponPart.class);
//                weapon.setTarget(target);
//                if (distance(p, target.getPart(PositionPart.class)) < weapon.getRange()) {
//                    weapon.process(gameData, target);   // Dont really know what to use as arguments   
//                }
//            }
            // For loop ensures only the last sent route has an effect on enemy movement
            for (Event event : gameData.getEvents(RouteCalculatedEvent.class, enemy.getID())) {
                RouteCalculatedEvent routeCalculatedEvent = (RouteCalculatedEvent) event;
                enemy.setCommands(routeCalculatedEvent.getEnemyCommands());
                gameData.removeEvent(event);
            }

            if (enemy.getCommands() != null && !enemy.getCommands().isEmpty() && !enemy.isDoneFollowingCommands()) {
                
                Command nextCommand = enemy.getCommands().get(enemy.getCommandIndex()).getCommand();
                moveEnemy(enemy, gameData);
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
        }
    }

    public void moveEnemy(Enemy enemy, GameData gameData) {
        EnemyCommand command = enemy.getCommands().get(enemy.getCommandIndex());
        float futureXPosition = enemy.calculateFutureXPosition();
        float futureYPosition = enemy.calculateFutureYPosition();

        float targetX = enemy.getXTarget();
        float targetY = enemy.getYTarget();
        
        // TODO - if the target is an attacking target, don't stand completly on it
        
        if (futureXPosition < targetX) {
            enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.RIGHT, "texturesprites/enemy/enemyright.atlas"));
        } else if (futureXPosition > targetX) {
            enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.LEFT, "texturesprites/enemy/enemyleft.atlas"));
        } else if (futureYPosition < targetY) {
            enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.UP, "texturesprites/enemy/enemyup.atlas"));
        } else if (futureYPosition < targetY) {
            enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.DOWN, "texturesprites/enemy/enemydown.atlas"));
        } else { // The enemy stands on the right tile.
            // Check if enemy should attack
            if (command.getCommand() == Command.ATTACK) {
                WeaponPart weaponPart = enemy.getWeaponPart();
                Tower towerToAttack = (Tower) command.getTarget();
                LifePart towerLifePart = towerToAttack.getPart(LifePart.class);
                // If Tower isn't dead
                if (!towerLifePart.isDead()) {
                    // Attack tower
                    weaponPart.process(gameData, towerToAttack);
                } else {
                    enemy.incrementCommandIndex();
                }
            } else {
                // Move towards the next command when either enemy has to move again or the tower to attack is dead
                enemy.incrementCommandIndex();
            }
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

    private float distance(PositionPart enemyPosPart, PositionPart towerPosPart) {
        float dx = (float) enemyPosPart.getX() - (float) towerPosPart.getX();
        float dy = (float) enemyPosPart.getY() - (float) towerPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
