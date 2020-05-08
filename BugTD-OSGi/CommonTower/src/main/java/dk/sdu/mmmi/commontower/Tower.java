package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.weaponpart.WeaponPart;

public class Tower extends Entity {

    public Tower(PositionPart posPart, LifePart lifePart, CollisionPart collisionPart, WeaponPart weaponPart, SpritePart spritePart) {
        setRadius(10);
        add(posPart);
        add(lifePart);
        add(collisionPart);
        add(weaponPart);
        add(spritePart);
    }
}
