/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weapon;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.MapChangedDuringRoundEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commontower.Tower;
import dk.sdu.mmmi.commonweapon.WeaponPart;

/**
 *
 * @author marcu
 */
public class WeaponControlSystem implements IEntityProcessingService {

    private MapSPI map;
    @Override
    public void process(GameData gameData, World world) {
        for(Entity e: world.getEntities()){
            if(e.getPart(WeaponPart.class) != null){
                WeaponPart weapon = e.getPart(WeaponPart.class);
                if(weapon.getTarget() != null && map.distance(e, weapon.getTarget()) <= weapon.getRange()){
                    weapon.process(gameData, e);
                }
            }
        }
    }
    
    public void setMapSPI(MapSPI spi) {
        this.map = spi;
    }

    public void removeMapSPI(MapSPI spi) {
        this.map = null;
    }
    
}
