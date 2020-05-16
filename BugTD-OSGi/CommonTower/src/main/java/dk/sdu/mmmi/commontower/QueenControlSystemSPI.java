package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface QueenControlSystemSPI {
    
    void attackEnemies(GameData gameData, World world);
    
    Entity calculateClosestEnemy(World world);
    
}
