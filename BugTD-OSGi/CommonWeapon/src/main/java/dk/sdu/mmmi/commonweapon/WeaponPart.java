package dk.sdu.mmmi.commonweapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.EntityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;

public class WeaponPart implements EntityPart {

    private Entity target;
    private float damage;
    private float range;
    private float speed;
    private boolean isAttacking = false;
    private boolean isNewTarget = false;
    public ShapeRenderer sr;
    private int color[] = {1, 1, 1, 1};
    // TODO: private Damagetype dt

    float cooldown = 0;
    float attackflash = 0;

    public boolean inRange = false;

    public enum Color {
        WHITE,
        YELLOW,
        BLUE,
        GREEN,
        RED
    }

    public WeaponPart(float damage, float range, float speed) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;

    }

    public WeaponPart(float damage, float range, float speed, Color c) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        setColor(c);

    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void setInRange(boolean b) {
        this.inRange = b;
    }

    public void setColor(Color colorpart) {
        switch (colorpart) {
            case WHITE:
                color[0] = 1;
                color[1] = 1;
                color[2] = 1;
                color[3] = 1;
                break;

            case YELLOW:
                color[0] = 1;
                color[1] = 1;
                color[2] = 0;
                color[3] = 1;
                break;

            case BLUE:
                color[0] = 0;
                color[1] = 0;
                color[2] = 1;
                color[3] = 1;
                break;

            case GREEN:
                color[0] = 0;
                color[1] = 1;
                color[2] = 0;
                color[3] = 1;
                break;

            case RED:
                color[0] = 1;
                color[1] = 0;
                color[2] = 0;
                color[3] = 1;

        }
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

    public float getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setIsNewTarget(boolean newTarget) {
        this.isNewTarget = newTarget;
    }

    @Override
    public void process(GameData gameData, Entity source) {
        LifePart targetLife = target.getPart(LifePart.class);
        if (targetLife != null) {

            // Prevents it from showing attackflash from previous target on new target
            if (isNewTarget) {
                isNewTarget = false;
                attackflash = 0;
            }

            if (targetLife.getLife() <= 0) {
                target = null;
            } else {
                // Check whether the Weapon is ready to shoot or not
                if (cooldown <= 0) {
                    cooldown = speed;   // Reset cooldown
                    targetLife.setLife(targetLife.getLife() - damage);    // Damage entity
                    attackflash = 0.15f;
                }

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
                }
            }
        }
        cooldown -= gameData.getDelta();    // Slowly decreasing the cooldown
        attackflash -= gameData.getDelta();
    }

    public void drawAttack(float x1, float y1, float x2, float y2) {
        sr = new ShapeRenderer();
        sr.setColor(color[0], color[1], color[2], color[3]);
        Gdx.gl.glLineWidth(2);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(x1, y1, x2, y2);
        sr.end();
    }
}
