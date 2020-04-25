/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.*;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.TileSizes;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Jakob
 */
public class PlayerControlSystem implements IEntityProcessingService {
    
    private MapSPI map;
    
    public void setMapSPI(MapSPI spi){
        this.map = spi;
    }

    @Override
    public void process(GameData gameData, World world) {
         for (Entity player : world.getEntities(Player.class)){
             
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            AnimationPart animationPart = player.getPart(AnimationPart.class);
            
            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setUp(gameData.getKeys().isDown(DOWN));

            
            setAnimation(player, gameData, animationPart);
            movingPart.process(gameData, player);
            positionPart.process(gameData, player);


            // for later setup
            movePlayer(gameData, player, world, animationPart);
 
        }
         
    }
    
    
    public void getTargetX(GameData gameData){
        
        List <Event> events = new ArrayList<>();
        
        for (Event event : gameData.getEvents()){
            if (!(event instanceof ClickEvent)){
                continue;
            }
            
            events.add(event);
            
            int clickX = ((ClickEvent) event).getX();
            
            clickX = roundDown(clickX, TileSizes.GRASS_WIDTH);
            
            System.out.println(clickX);
        }
        gameData.getEvents().removeAll(events);
        
    }
    
    public void movePlayer(GameData gameData, Entity player, World world, AnimationPart ani){
        
        float targetX;
        float targetY;
        
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
                clickX = roundDown(clickY, TileSizes.GRASS_WIDTH);
                targetX = clickX; 
                targetY = clickY;
                
                System.out.println(clickX);

                mov.setUp(false);
                mov.setDown(false);
                mov.setLeft(false);
                mov.setRight(false);


                    
                    if (pos.getX() != targetX){

                        if (pos.getX() < targetX){
                            mov.setRight(true);
                            ani.setAtlasPath("texturesprites/enemy/enemydown.atlas");
                        }
                        if (pos.getX() > targetX){
                            mov.setLeft(true);
                            ani.setAtlasPath("texturesprites/enemy/enemyup.atlas");
                        }
                        if (pos.getY() < targetY){
                            mov.setUp(true);
                            ani.setAtlasPath("texturesprites/enemy/enemyright.atlas");
                        }
                        if (pos.getY() > targetY){
                            mov.setDown(true);
                            ani.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
                        }
                    } else{
                        mov.setUp(false);
                        mov.setDown(false);
                        mov.setLeft(false);
                        mov.setRight(false);
                    }
                }

            
            gameData.getEvents().removeAll(events);
        }    
       
    }
    
    public void test(){
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        double x = p.getX();
        System.out.println(x);
        
    }
    
    private Point getCurrentCursorLocation(){
        Point res = null;
        PointerInfo pi = MouseInfo.getPointerInfo();
        if( null != pi){
            res = pi.getLocation();
        }
        System.out.println(res);
        return res;
    }
    
//    public float getPosX(int xpos, int ypos){
//        float x = xpos;
//        float y = ypos; 
//        float radians = 0;
//        
//        PositionPart pos = new PositionPart(x,y,radians);
//        
//        return pos.getX();
//    }
//    
//    public float getPosY(int xpos, int ypos){
//        float x = xpos;
//        float y = ypos; 
//        float radians = 0;
//        
//        PositionPart pos = new PositionPart(x,y,radians);
//        
//        return pos.getY();
//    }
//    
//    public void setTargetX(){
//        this.targetX = targetX;
//    }
//    
//    public void setTargetY(){
//        this.targetY = targetY;
//    }
//    
//    public void touchDown(InputEvent event, float x, float y, int pointer){
//        Vector3 mousePos = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
//        System.out.println(mousePos);
//    }
//    
//    
//    public void placement(GameData gameData, World world){
//
//        if (test == null){
//            Placement place = new Placement(new PositionPart(0,0,0));
//            world.addEntity(test);
//            test = place;
//        }
//        
//        
//        PositionPart posPart = test.getPart(PositionPart.class);
//        int x = roundDown(gameData.getMouseX(), TileSizes.GRASS_WIDTH);
//        posPart.getX();
//        //System.out.println(posPart.getX());
//        System.out.println(x);
//        //return posPart.getX();
//        
//    }
    
//    public void test(){
//        Vector3 mousePos = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
//        System.out.println(mousePos);
//        
//        if (Gdx.input.justTouched()){
//            System.out.println("ayy");
//        }
//    }
    
    public void movePlayerOnClick(Entity player, PositionPart pos, MovingPart mov, AnimationPart ani){
        

        float targetX = 0;
        float targetY = pos.getX();
        
        float futureXPos;
        float futureYPos;
        
//        Vector3 mousePos = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
//        System.out.println(mousePos);
        
        //GameKeys keys = new GameKeys();

        mov.setUp(false);
        mov.setDown(false);
        mov.setLeft(false);
        mov.setRight(false);
            
//        while(!keys.isDown(UP) || !keys.isDown(DOWN) || 
//                !keys.isDown(LEFT) || !keys.isDown(RIGHT)){
            
            if (pos.getX() != targetX){
                
                if (pos.getX() < targetX){
                    mov.setRight(true);
                    //ani.setAtlasPath("");
                }
                if (pos.getX() > targetX){
                    mov.setLeft(true);
                    //ani.setAtlasPath("");
                }
                if (pos.getY() < targetY){
                    mov.setUp(true);
                    //ani.setAtlasPath("");
                }
                if (pos.getY() > targetY){
                    mov.setDown(true);
                    //ani.setAtlasPath("");
                }
            }
//        }

    }
        
    
    public void setAnimation(Entity player, GameData gameData, AnimationPart ani){

        // dont know if animation is needed for up and down, when the player model switches from side to side and not up and down
//        if (gameData.getKeys().isDown(UP)){
//            throw new UnsupportedOperationException("Something about atlas files "); 
//        }
//        if (gameData.getKeys().isDown(DOWN)){
//            throw new UnsupportedOperationException("Something about atlas files "); 
//        }
//        if (gameData.getKeys().isDown(LEFT)){
//            //ani.setAtlasPath("texturesprites/player64/player64.atlas");
//            ani.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
//           // throw new UnsupportedOperationException("Something about atlas files "); 
//        }
//        if (gameData.getKeys().isDown(RIGHT)){
//            //ani.setAtlasPath("texturesprites/player64/player64.atlas");
//            ani.setAtlasPath("texturesprites/enemy/enemyright.atlas");
//            //throw new UnsupportedOperationException("Something about atlas files "); 
//        }

        if (gameData.getKeys().isDown(DOWN)) {
            ani.setAtlasPath("texturesprites/enemy/enemydown.atlas");
        }
        if(gameData.getKeys().isDown(UP)) {
            ani.setAtlasPath("texturesprites/enemy/enemyup.atlas");
        }
        if(gameData.getKeys().isDown(RIGHT)){
            ani.setAtlasPath("texturesprites/enemy/enemyright.atlas");
        }
        if(gameData.getKeys().isDown(LEFT)){
            ani.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
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

