/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;


/**
 *
 * @author Jakob
 */
public class PlayerPlugin implements IGamePluginService  {
    
    //private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        
        Entity aPlayer = new Player();
        
        // attributes
         float deacceleration  = 100;
         float acceleration = 200;
         float maxSpeed = 500;
         float rotation = 5;
         float x = gameData.getDisplayWidth() / 3;
         float y = gameData.getDisplayWidth() / 2;
         float radians = 3.1415f / 2;
         
         // parts 
         //AnimationPart animation = new AnimationPart("texturesprites/player64/stand_left_64.atlas", 32, 32);
         AnimationPart animation = new AnimationPart("texturesprites/player32/stand.atlas", 32, 32);
         aPlayer.add(animation);

         MovingPart moving = new MovingPart(deacceleration,acceleration,maxSpeed, rotation );
         aPlayer.add(moving);
         PositionPart position = new PositionPart(x,y,radians);
         aPlayer.add(position);
         
//         aPlayer.add(animation);
//         aPlayer.add(moving);
//         aPlayer.add(position);
         System.out.println("hello");
         world.addEntity(aPlayer);
    }
    

    @Override
    public void stop(GameData gameData, World world) {
       for (Entity player : world.getEntities(Player.class)){
           world.removeEntity(player);
       }
    }


    
}
