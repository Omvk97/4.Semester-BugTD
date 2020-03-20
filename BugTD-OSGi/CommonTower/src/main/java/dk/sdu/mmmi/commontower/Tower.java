package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;

public class Tower extends Entity{
    
    private float shootingRadius;
    
    public Tower(PositionPart posPart, LifePart lifePart, WeaponPart weaponPart) {     // TODO: CollisionPart
        add(posPart);
        add(lifePart);
    }
}
