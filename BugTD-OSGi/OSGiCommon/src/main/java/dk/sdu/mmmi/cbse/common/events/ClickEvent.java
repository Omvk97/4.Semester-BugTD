package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class ClickEvent extends Event {
    
    private int x;
    private int y;

    public ClickEvent(Entity source, int screenX, int screenY) {
        super(source);
        x = screenX;
        y = screenY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}