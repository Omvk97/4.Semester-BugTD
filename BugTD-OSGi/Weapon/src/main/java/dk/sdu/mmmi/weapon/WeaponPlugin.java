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
import dk.sdu.mmmi.commonweapon.WeaponPart;

/**
 *
 * @author marcu
 */
public class WeaponPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity e: world.getEntities()){
            if(e.getPart(WeaponPart.class) != null){
                WeaponPart weapon = e.getPart(WeaponPart.class);
                weapon.setTarget(null);
            }
        }
    }
    
}
