package dk.sdu.mmmi.commonplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Player extends Entity {
    private boolean hasTarget;

    public boolean hasTarget() {
        return hasTarget;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }
}
