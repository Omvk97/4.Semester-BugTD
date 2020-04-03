package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;

public class TowerPreview extends Entity {

    public TowerPreview(PositionPart posPart, SpritePart spritePart) {
        add(posPart);
        add(spritePart);
    }
}
