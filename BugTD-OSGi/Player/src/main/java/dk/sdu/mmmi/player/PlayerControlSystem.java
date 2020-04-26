/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.*;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.TileSizes;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Jakob
 */
public class PlayerControlSystem implements IEntityProcessingService {
    
//    private MapSPI map;
//    
//    public void setMapSPI(MapSPI spi){
//        this.map = spi;
//    }
    
    // todo 
    // 
    float targetX;
    float targetY;

    @Override
    public void process(GameData gameData, World world) {
         for (Entity player : world.getEntities(Player.class)){
             
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            AnimationPart animationPart = player.getPart(AnimationPart.class);
            
            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setDown(gameData.getKeys().isDown(DOWN));

            
            setAnimation(player, gameData, animationPart);
            movingPart.process(gameData, player);
            positionPart.process(gameData, player);


            movePlayer(gameData, player, world, animationPart);
 
        }
         
    }
    
    
    public void movePlayer(GameData gameData, Entity player, World world, AnimationPart ani){
        
//        float targetX;
//        float targetY;
        
        for (Entity player1 : world.getEntities(Player.class)){
            
            PositionPart pos = player1.getPart(PositionPart.class);
            MovingPart mov = player1.getPart(MovingPart.class);
            
            
            List <Event> events = new ArrayList<>();

            for (Event event : gameData.getEvents()){
                if (!(event instanceof ClickEvent)){
                    continue;
                }

                events.add(event);

                int clickX = ((ClickEvent) event).getX();
                int clickY = ((ClickEvent) event).getY();
                clickX = roundDown(clickX, TileSizes.GRASS_WIDTH);
                clickY = roundDown(clickY, TileSizes.GRASS_WIDTH);
                targetX = clickX; 
                targetY = clickY;
                
                System.out.println(clickX);

                mov.setUp(false);
                mov.setDown(false);
                mov.setLeft(false);
                mov.setRight(false);


                    // change atlasPath
                    //if (pos.getX() != targetX && pos.getY() != targetY){
                    

                        if (pos.getX() < targetX){
//                            mov.setRight(true);
                            pos.setX(targetX);
                            ani.setAtlasPath("texturesprites/player32/right.atlas");
                        }
                        if (pos.getX() > targetX){
                            //mov.setLeft(true);
                            pos.setX(targetX);
                            ani.setAtlasPath("texturesprites/player32/left.atlas");
                        }
                        if (pos.getY() < targetY){
                            //mov.setUp(true);
                            pos.setY(targetY);
                            ani.setAtlasPath("texturesprites/player32/stand.atlas");
                        }
                        if (pos.getY() > targetY){
                            //mov.setDown(true);
                            pos.setY(targetY);
                            ani.setAtlasPath("texturesprites/player32/stand.atlas");
                        }
                    //} 
                }

            
            gameData.getEvents().removeAll(events);
        }    
       
    }
//    
//    public void test(){
//        
//        Point p = MouseInfo.getPointerInfo().getLocation();
//        double x = p.getX();
//        System.out.println(x);
//        
//    }
//    
//    private Point getCurrentCursorLocation(){
//        Point res = null;
//        PointerInfo pi = MouseInfo.getPointerInfo();
//        if( null != pi){
//            res = pi.getLocation();
//        }
//        System.out.println(res);
//        return res;
//    }
          
    
    public void setAnimation(Entity player, GameData gameData, AnimationPart ani){


        if (gameData.getKeys().isDown(DOWN)) {
            ani.setAtlasPath("texturesprites/player32/stand.atlas");
        }
        if(gameData.getKeys().isDown(UP)) {
            ani.setAtlasPath("texturesprites/player32/stand.atlas");
        }
        if(gameData.getKeys().isDown(RIGHT)){
            ani.setAtlasPath("texturesprites/player32/right.atlas");
        }
        if(gameData.getKeys().isDown(LEFT)){
            ani.setAtlasPath("texturesprites/player32/left.atlas");
        }

    }
    
    public void playerWithinRangeOfTower(World world, PositionPart pos){
         throw new UnsupportedOperationException("Not supported yet."); 
         
         
    }
    

    
    public boolean isKeyPressed(GameData gameData){
        
        if (gameData.getKeys().isDown(DOWN)|| gameData.getKeys().isDown(UP)
                || gameData.getKeys().isDown(RIGHT) || gameData.getKeys().isDown(LEFT)){
            return true;
        }
        return false;
    }
    
    
    public int  roundDown(double number, double place){
        double result = number / place;
        result = Math.floor(result);
        result *= place;
        return (int) result;
    }
    
    

    
}

