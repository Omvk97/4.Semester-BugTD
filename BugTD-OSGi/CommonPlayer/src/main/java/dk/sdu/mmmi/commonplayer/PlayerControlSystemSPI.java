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
    
    boolean isKeyPressed(GameData gameData);
    
    int roundDown(double number, double place);
    
    void moveHorizontal(Entity e, float distance);
    
    void moveVertical(Entity e, float speed);
    
    
    
    
}
