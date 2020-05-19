package dk.sdu.mmmi.cbse.common.data;

import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventObserver;
import dk.sdu.mmmi.cbse.common.events.EventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {

    private float delta;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();
    private final List<Event> events = new CopyOnWriteArrayList<>();
    private final Map<EventObserver, EventType[]> observers = new HashMap<>();
    private int mouseX;
    private int mouseY;
    private int difficulty = 1;
    private String menuFlashMessage;

    public String getMenuFlashMessage() {
        return menuFlashMessage;
    }

    public void setMenuFlashMessage(String menuFlashMessage) {
        this.menuFlashMessage = menuFlashMessage;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void addEvent(Event e) {
        events.add(e);
        if (e.getType() != null) {
            Iterator it = observers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                EventObserver key = (EventObserver) pair.getKey();
                EventType[] value = (EventType[]) pair.getValue();
                for (EventType eventType : value) {
                    if (eventType.equals(e.getType())) {
                        key.methodToCall(e);
                        if (key.getRemoveEvent()) {
                            events.remove(e);
                        }
                    }
                }
            }
        }
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    public List<Event> getEvents() {
        return events;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public <E extends Event> List<Event> getEvents(Class<E> type) {
        List<Event> r = new ArrayList();
        for (Event event : events) {
            if (event.getClass().equals(type)) {
                r.add(event);
            }
        }

        return r;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public <E extends Event> List<Event> getEvents(Class<E> type, String sourceID) {
        List<Event> r = new ArrayList();
        for (Event event : events) {
            if (event.getClass().equals(type) && event.getSource().getID().equals(sourceID)) {
                r.add(event);
            }
        }

        return r;
    }

    public <E extends Event> void removeEvents(Class<E> type) {
        for (Event event : events) {
            if (event.getClass().equals(type)) {
                events.remove(event);
            }
        }
    }

    public void listenForEvent(EventObserver observer, EventType... eventsToListenFor) {
        observers.put(observer, eventsToListenFor);
    }

    public void removeListener(EventObserver observer) {
        observers.remove(observer);
    }
}
