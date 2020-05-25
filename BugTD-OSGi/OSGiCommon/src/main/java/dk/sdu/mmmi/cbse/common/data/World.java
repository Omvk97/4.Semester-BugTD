package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, HashMap<String, Entity>> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        HashMap<String, Entity> entities = entityMap.get(entity.getClass().toString());
        if (entities == null) { // first time an entity of that type has been added to the world
            entities = new HashMap<>();
            entities.put(entity.getID(), entity);
            entityMap.put(entity.getClass().toString(),entities);
        } else {
            entities.put(entity.getID(), entity);
        }
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        for (HashMap<String, Entity> le : entityMap.values()) {
            Entity removedEntity = le.remove(entityID);
            if (removedEntity != null) { // The entity was found and we should therefor stop searching for it
                return;
            }
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntity(entity.getID());
    }

    public Collection<Entity> getEntities() {
        List<Entity> allEntities = new ArrayList<>();
        for (HashMap<String, Entity> le : entityMap.values()) {
            allEntities.addAll(le.values());
        }
        return allEntities;
    }

    public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> entitesFound = new ArrayList<>();
        for (Class<E> entityType : entityTypes) {
            HashMap<String, Entity> e = entityMap.get(entityType.toString());
            if (e != null) {
                entitesFound.addAll(e.values());
            }
        }

        return entitesFound;
    }

    public Entity getEntity(String ID) {
        for (HashMap<String, Entity> le : entityMap.values()) {
            Entity foundEntity = le.get(ID);
            if (foundEntity != null) {
                return foundEntity;
            }
        }
        return null;
    }

}
