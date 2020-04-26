package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;

public class TowerPlacedDuringRoundEvent extends Event {

    public TowerPlacedDuringRoundEvent(Entity source) {
        super(source);
    }

    public Entity getTower() {
        return source;
    }

}
