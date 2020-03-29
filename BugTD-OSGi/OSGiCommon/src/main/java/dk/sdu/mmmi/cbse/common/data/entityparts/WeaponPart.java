package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class WeaponPart implements EntityPart {

    private Entity target;
    private float damage;
    private float range;
    private float speed;
    // TODO: private Damagetype dt

    float cooldown = 0;

    public WeaponPart(float damage, float range, float speed) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public float getRange() {
        return range;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        // Check whether the Weapon is ready to shoot or not
        if (cooldown <= 0) {
            cooldown = speed;   // Reset cooldown
            LifePart lp = entity.getPart(LifePart.class);
            lp.setLife(lp.getLife() - (int) damage);    // Damage entity
            lp.process(gameData, entity);
            System.out.println("Damaged enemy: " + lp.getLife());
        }

        cooldown -= gameData.getDelta();    // Slowly decreasing the cooldown
    }
}
