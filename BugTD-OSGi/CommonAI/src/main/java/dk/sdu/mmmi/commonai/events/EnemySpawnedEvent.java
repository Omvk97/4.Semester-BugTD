package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;

public class EnemySpawnedEvent extends Event {

    public EnemySpawnedEvent(Entity source) {
        super(source);
    }

    public Entity getEnemy() {
        return source;
    }

}
