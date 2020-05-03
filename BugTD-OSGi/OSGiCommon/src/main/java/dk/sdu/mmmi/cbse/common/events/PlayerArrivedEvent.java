package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class PlayerArrivedEvent extends Event {

    private int x;
    private int y;

    public PlayerArrivedEvent(Entity source) {
        super(source);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
