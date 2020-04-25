package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class GameWonEvent extends Event {
    
    public GameWonEvent(Entity source){
        super(source);
    }
}
