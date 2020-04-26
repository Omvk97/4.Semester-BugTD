package dk.sdu.mmmi.cbse.common.data.entityparts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class WeaponPart implements EntityPart {

    private Entity target;
    private float damage;
    private float range;
    private float speed;
    private boolean isAttacking = false;
    public ShapeRenderer sr;
    // TODO: private Damagetype dt

    float cooldown = 0;
    float attackflash = 0;

    public WeaponPart(float damage, float range, float speed) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;

    }

    public void setTarget(Entity target) {
        this.target = target;
    }
    
    public Entity getTarget(){
        return target;
    }

    public float getRange() {
        return range;
    }
    public void setAttackingStatus(boolean attacking) {
        this.isAttacking = attacking;
    }

    public boolean getAttackingStatus() {
        return this.isAttacking;
    }
    
    public void drawAttack(float x1, float y1, float x2, float y2){
        sr = new ShapeRenderer();
        sr.setColor(1, 1, 0, 1);
        Gdx.gl.glLineWidth(2);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(x1, y1, x2, y2);
        sr.end();
    }
    
    @Override
    public void process(GameData gameData, Entity source) {
        // Check whether the Weapon is ready to shoot or not
        
        if (cooldown <= 0) {
            cooldown = speed;   // Reset cooldown
            LifePart lp = target.getPart(LifePart.class);
            lp.setLife(lp.getLife() - damage);    // Damage entity
            attackflash = 0.15f;
            System.out.println("Life: " + lp.getLife());
        }
        
        //Checks if it should draw attack
        if (attackflash > 0) {
            PositionPart pPart1 = source.getPart(PositionPart.class);
            PositionPart pPart2 = target.getPart(PositionPart.class);
            
            SpritePart sPart1 = source.getPart(SpritePart.class);
            SpritePart sPart2 = target.getPart(SpritePart.class);
            AnimationPart aPart1 = source.getPart(AnimationPart.class);
            AnimationPart aPart2 = target.getPart(AnimationPart.class);
            
            float x1 = pPart1.getX() + (sPart1 == null ? (aPart1.getWidth() / 2) : (sPart1.getWidth() / 2));
            float y1 = pPart1.getY() + (sPart1 == null ? (aPart1.getHeight() / 2) : (sPart1.getHeight() / 2));
            float x2 = pPart2.getX() + (sPart2 == null ? (aPart2.getWidth() / 2) : (sPart2.getWidth() / 2));
            float y2 = pPart2.getY() + (sPart2 == null ? (aPart2.getHeight() / 2) : (sPart2.getHeight() / 2));
            
            drawAttack(x1, y1, x2, y2);
            attackflash -= gameData.getDelta();
        }

        cooldown -= gameData.getDelta();    // Slowly decreasing the cooldown
        
    }
}
