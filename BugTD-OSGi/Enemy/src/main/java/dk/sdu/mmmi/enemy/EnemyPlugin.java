/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonweapon.WeaponPart;

/**
 *
 * @author oliver
 */
public class EnemyPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Enemy.enemyRemovedFromGame = false;
    }

    @Override
    public void stop(GameData gameData, World world) {
        Enemy.enemyRemovedFromGame = true;
        
        
        for(Entity e: world.getEntities()){
            if(e.getPart(WeaponPart.class) != null){
                WeaponPart weapon = e.getPart(WeaponPart.class);
                if (weapon.getTarget() != null && weapon.getTarget().getClass().equals(Enemy.class)) {
                    weapon.setTarget(null);
                }
            }
        }
        
        // Removing all enemies
        for (Entity en : world.getEntities(Enemy.class)) {
            world.removeEntity(en);
        }
        
        
    }
    
}
