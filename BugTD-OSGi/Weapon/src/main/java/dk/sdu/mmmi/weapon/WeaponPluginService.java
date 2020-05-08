/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.weaponpart.WeaponPart;

/**
 *
 * @author marcu
 */
public class WeaponPluginService implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Weapon Plugin Service Initialized");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity entity : world.getEntities()){
            if(entity.getPart(WeaponPart.class) != null){
                entity.remove(WeaponPart.class);
                System.out.println("All Weapons Removed");
            }
        }
    }
    
}
