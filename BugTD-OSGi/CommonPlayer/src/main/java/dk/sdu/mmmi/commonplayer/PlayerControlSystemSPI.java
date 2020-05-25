/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 *
 * @author Jakob
 */
public interface PlayerControlSystemSPI {
    
    void movePlayer(GameData gameData, World world);

    Entity calculateClosestEnemy(World world, Entity entity);
    
    void attackEnemies(GameData gameData, World world); 
    
    
    
    
}
