package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class CollisionEvent extends Event{
    
    private final Entity source2;
    
    public CollisionEvent(Entity source, Entity source2) {
        super(source);
        this.source2 = source2;
    }
    
    public Entity getSecondSource() {
        return source2;
    }
}
