/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 *
 * @author marcu
 */
public class ShootingPart implements EntityPart {

    private static ShapeRenderer sr = new ShapeRenderer();
    @Override
    public void process(GameData gameData, Entity entity) {
        PositionPart pPart1 = entity.getPart(PositionPart.class);
//        WeaponPart wPart = entity.getPart(WeaponPart.class);
//        if(wPart.getTarget() != null){
//            Entity target = wPart.getTarget();
//            PositionPart pPart2 = target.getPart(PositionPart.class);
//            float x1 = pPart1.getX();
//            float y1 = pPart1.getY();
//            float x2 = pPart2.getX();
//            float y2 = pPart2.getY();
//            drawAttack(x1, y1, x2, y2);
//            
//        }
        
        
        
    }
    
    private void drawAttack(float x1, float y1, float x2, float y2){
//        sr.begin(ShapeType.Line);
//        sr.setColor(1, 0, 0, 0);
//        sr.line(x1, y1, x2, y2);
//        sr.end();
        
    }
    
}
