package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import java.util.LinkedList;
import java.util.Queue;

public class PreciseMovingPart extends MovingPart {

    public enum Movement {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    private Queue<PreciseMovementInstruction> movements = new LinkedList<>();

    public PreciseMovingPart(float speedPerAction) {
        super(0, speedPerAction, 0, 0);
    }

    public void addMovement(PreciseMovementInstruction instruction) {
        this.movements.add(instruction);
    }

    public float calculateFutureXPosition(float currentX) {
        for (PreciseMovementInstruction nextMovement : this.movements) {
            switch (nextMovement.getMovementDirection()) {
                case LEFT:
                    currentX -= calculateMovement();
                    break;
                case RIGHT:
                    currentX += calculateMovement();
                    break;
            }
        }
        return currentX;
    }

    public float calculateFutureYPosition(float currentY) {
        for (PreciseMovementInstruction nextMovement : this.movements) {
            switch (nextMovement.getMovementDirection()) {
                case DOWN:
                    currentY -= calculateMovement();
                    break;
                case UP:
                    currentY += calculateMovement();
                    break;
            }
        }
        return currentY;
    }

    private float calculateMovement() {
        return this.acceleration;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        PositionPart positionPart = entity.getPart(PositionPart.class);
        AnimationPart animationPart = entity.getPart(AnimationPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();

        PreciseMovementInstruction nextMovement = this.movements.poll();
        if (nextMovement != null) {
            animationPart.setAtlasPath(nextMovement.getAtlasPath());

//            float delta = nextMovement.getDelta();
            switch (nextMovement.getMovementDirection()) {
                case LEFT:
                    x -= calculateMovement();
                    break;
                case RIGHT:
                    x += calculateMovement();
                    break;
                case DOWN:
                    y -= calculateMovement();
                    break;
                case UP:
                    y += calculateMovement();
                    break;
            }
        }

        positionPart.setX(x);
        positionPart.setY(y);
    }

}
