package dk.sdu.mmmi.commonai;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

public interface AIProcessingServiceSPI {

    Entity calculateClosestTower(World world, Entity enemy);
}
