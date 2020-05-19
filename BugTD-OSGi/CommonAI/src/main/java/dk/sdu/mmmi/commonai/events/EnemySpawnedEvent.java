package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;

public class EnemySpawnedEvent extends Event {

    public EnemySpawnedEvent(Entity source) {
        super(source, EventType.EnemySpawnedEvent);
    }

    public Entity getEnemy() {
        return source;
    }

}
