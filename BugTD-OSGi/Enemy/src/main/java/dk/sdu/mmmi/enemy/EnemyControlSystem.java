package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovementInstruction;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovingPart;
import dk.sdu.mmmi.cbse.common.events.EnemyDiedEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.Command;
import dk.sdu.mmmi.commonai.events.EnemyCommand;
import dk.sdu.mmmi.commonai.events.RouteCalculatedEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commontower.Tower;
import dk.sdu.mmmi.commonweapon.WeaponPart;
import java.awt.MouseInfo;
import java.awt.Point;

public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;

            if (((LifePart) enemy.getPart(LifePart.class)).isDead()) {
                gameData.addEvent(new EnemyDiedEvent(enemy));
                world.removeEntity(enemy);
            }

            // AI Processing
            PositionPart positionPart = enemy.getPositionPart();
            PreciseMovingPart movingPart = enemy.getMovingPart();

            // For loop ensures only the last sent route has an effect on enemy movement
            for (Event event : gameData.getEvents(RouteCalculatedEvent.class, enemy.getID())) {
                RouteCalculatedEvent routeCalculatedEvent = (RouteCalculatedEvent) event;
                enemy.setCommands(routeCalculatedEvent.getEnemyCommands());
                gameData.removeEvent(event);
            }

            if (enemy.getCommands() != null && !enemy.getCommands().isEmpty() && !enemy.isDoneFollowingCommands()) {
                moveAndAttack(enemy, gameData);
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
        }
    }

    public void moveAndAttack(Enemy enemy, GameData gameData) {
        EnemyCommand command = enemy.getCommands().get(enemy.getCommandIndex());
        float futureXPosition = enemy.calculateFutureXPosition();
        float futureYPosition = enemy.calculateFutureYPosition();

        float targetX = enemy.getXTarget();
        float targetY = enemy.getYTarget();

        if (command.getCommand() == Command.WALK) {
            if (futureXPosition < targetX) {
                enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.RIGHT, "texturesprites/enemy/enemyright.atlas"));
            } else if (futureXPosition > targetX) {
                enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.LEFT, "texturesprites/enemy/enemyleft.atlas"));
            } else if (futureYPosition < targetY) {
                enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.UP, "texturesprites/enemy/enemyup.atlas"));
            } else if (futureYPosition > targetY) {
                enemy.addMovement(new PreciseMovementInstruction(PreciseMovingPart.Movement.DOWN, "texturesprites/enemy/enemydown.atlas"));
            } else {
                enemy.incrementCommandIndex();
            }
        } else {
            // TODO - if the target is an attacking target, don't stand completly on it// The enemy stands on the right tile.
            // Check if enemy should attack
            WeaponPart weaponPart = enemy.getWeaponPart();
            Entity target = command.getTarget();
            LifePart targetLifePart = target.getPart(LifePart.class);
            // If Tower isn't dead
            if (!targetLifePart.isDead()) {
                // Attack tower
                weaponPart.setTarget(target);
            } else {
                // Move towards the next command when either enemy has to move again or the tower to attack is dead
                enemy.incrementCommandIndex();
            }
        }
    }

    private float distance(PositionPart enemyPosPart, PositionPart towerPosPart) {
        float dx = (float) enemyPosPart.getX() - (float) towerPosPart.getX();
        float dy = (float) enemyPosPart.getY() - (float) towerPosPart.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
