/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

/**
 *
 * @author oliver
 */
public class PreciseMovementInstruction {

    private final PreciseMovingPart.Movement movementDirection;
    private final String atlasPath;
    // TODO?
    private float delta;

    public PreciseMovementInstruction(PreciseMovingPart.Movement movementDirection, String atlasPath) {
        this.movementDirection = movementDirection;
        this.atlasPath = atlasPath;
    }

    public PreciseMovingPart.Movement getMovementDirection() {
        return movementDirection;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public float getDelta() {
        return delta;
    }
}
