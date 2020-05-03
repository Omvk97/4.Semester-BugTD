package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;

public class EnemyCommand {

    private final Entity target;
    private final Command command;

    public EnemyCommand(Entity target, Command command) {
        this.target = target;
        this.command = command;
    }

    public float getX() {
        PositionPart positionPart = target.getPart(PositionPart.class);
        return positionPart.getX();
    }

    public float getY() {
        PositionPart positionPart = target.getPart(PositionPart.class);
        return positionPart.getY();
    }

    public Entity getTarget() {
        return target;
    }

    public Command getCommand() {
        return command;
    }
}
