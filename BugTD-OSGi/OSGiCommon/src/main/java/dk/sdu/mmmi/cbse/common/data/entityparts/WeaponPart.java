package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class WeaponPart implements EntityPart {

    private Entity target;
    private float damage;
    private float range;
    private float speed;
    // TODO: private Damagetype dt

    public WeaponPart(float damage, float range, float speed) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

}
