package dk.sdu.mmmi.commonenemy;


import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.commonmap.Tile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author oliver
 */
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
