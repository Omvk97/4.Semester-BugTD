package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class IdentityPart implements EntityPart{
    
    private EntityIdentity identity;

    public IdentityPart(EntityIdentity identity) {
        this.identity = identity;
    }
    
    @Override
    public void process(GameData gameData, Entity entity) {
    }
    
    public EntityIdentity getIdentity() {
        return this.identity;
    }
    
    public enum EntityIdentity {
        Asteroid,
        Player,
        Enemy,
        Bullet
    }
}
