package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;


public class EnemyDiedEvent extends Event {
    
    public EnemyDiedEvent(Entity source) {
        super(source);
    }
}
