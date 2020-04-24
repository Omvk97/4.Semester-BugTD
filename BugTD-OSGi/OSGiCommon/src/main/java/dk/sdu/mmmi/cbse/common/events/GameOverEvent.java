package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class GameOverEvent extends Event{

    public GameOverEvent(Entity source) {
        super(source);
    }

}
