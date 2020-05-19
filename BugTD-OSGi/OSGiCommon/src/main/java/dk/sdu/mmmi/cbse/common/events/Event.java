package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import java.io.Serializable;

public class Event implements Serializable {

    protected final Entity source;
    protected EventType type;

    public Event(Entity source) {
        this.source = source;
    }
    
    public Event(Entity source, EventType type) {
        this.source = source;
        this.type = type;
    }

    public Entity getSource() {
        return source;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
