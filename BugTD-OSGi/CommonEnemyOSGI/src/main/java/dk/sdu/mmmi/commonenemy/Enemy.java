package dk.sdu.mmmi.commonenemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovementInstruction;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import java.util.List;

/**
 *
 * @author oliver
 */
public class Enemy extends Entity {
   
    private List<EnemyCommand> commands = null;
    private int commandIndex = 0;
    private final EnemyType type;
    private final WeaponPart weaponPart;
    private final AnimationPart animationPart;
    private final PositionPart positionPart;
    private final PreciseMovingPart movingPart;
    private final LifePart lifePart;

    public Enemy(EnemyType type, WeaponPart weaponPart, AnimationPart animationPart, PositionPart positionPart, PreciseMovingPart movingPart, LifePart lifePart) {
        this.type = type;
        this.weaponPart = weaponPart;
        add(weaponPart);
        this.animationPart = animationPart;
        add(animationPart);
        this.positionPart = positionPart;
        add(positionPart);
        this.movingPart = movingPart;
        add(movingPart);
        this.lifePart = lifePart;
        add(lifePart);
    }

    public List<EnemyCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<EnemyCommand> moveCommands) {
        this.commands = moveCommands;
        resetCommandIndex();
    }
    
    public void incrementCommandIndex() {
        this.commandIndex++;
    }
    
    private void resetCommandIndex() {
        this.commandIndex = 0;
    }
    
    public int getCommandIndex() {
        return this.commandIndex;
    }

    public boolean isDoneFollowingCommands() {
        return commandIndex == commands.size() - 1;
    }
    
    public float getXTarget() {
        return commands.get(commandIndex).getX();
    }
    
    public float getYTarget() {
        return commands.get(commandIndex).getY();
    }

    public EnemyType getType() {
        return type;
    }
    
    public float getCurrentX() {
        return positionPart.getX();
    }
    
    public float getCurrentY() {
        return positionPart.getY();
    }
   
    public float calculateFutureXPosition() {
        return movingPart.calculateFutureXPosition(getCurrentX());
    }
    
    public float calculateFutureYPosition() {
        return movingPart.calculateFutureYPosition(getCurrentY());
    }
    
    public void addMovement(PreciseMovementInstruction preciseMovementInstruction) {
        movingPart.addMovement(preciseMovementInstruction);
    }

    public WeaponPart getWeaponPart() {
        return weaponPart;
    }

    public AnimationPart getAnimationPart() {
        return animationPart;
    }

    public PositionPart getPositionPart() {
        return positionPart;
    }

    public PreciseMovingPart getMovingPart() {
        return movingPart;
    }
    
    
}
