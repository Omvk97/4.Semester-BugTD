/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.weaponpart.WeaponPart;

/**
 *
 * @author marcu
 */
public class WeaponControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        System.out.println("Processing WeaponControlSystem");
        for(Entity entity : world.getEntities()){
            if(entity.getPart(WeaponPart.class) != null){
                entity.getPart(WeaponPart.class).process(gameData, entity);
            }
        }
    }

}
