package dk.sdu.mmmi.commonenemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;

public class Enemy extends Entity {

    public static Entity createGroundEnemy(float x, float y) {
        Entity gEnemy = new Enemy();
        //attributes
        float damage = 10;
        float range = 50;
        float speed = 10;
        float deacceleration = 280;
        float acceleration = 210;
        float maxSpeed = 150;
        float rotationSpeed = 5;
        float radians = 3.1415f / 2;
        int life = 100;

        //Parts
        AnimationPart anm = new AnimationPart("texturesprites/enemy/enemyup.atlas", 32, 32, 0);
        gEnemy.add(anm);

        //SpritePart sprite = new SpritePart("enemy/enemyup/up_01.png", 32, 32);
        //gEnemy.add(sprite);
        WeaponPart wpn = new WeaponPart(damage, range, speed);
        gEnemy.add(wpn);
        MovingPart mov = new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed);
        gEnemy.add(mov);
        PositionPart pos = new PositionPart(x, y, radians);
        gEnemy.add(pos);
        LifePart lif = new LifePart(life);
        gEnemy.add(lif);

        return gEnemy;
    }

    public static Entity createFlyingEnemy(float x, float y) {
        Entity fEnemy = new Enemy();
        //attributes
        float damage = 10;
        float range = 50;
        float speed = 10;
        float deacceleration = 100;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float radians = 3.1415f / 2;
        int life = 5;

        //Parts
        AnimationPart anm = new AnimationPart("texturesprites/enemy/enemyup.atlas", 32, 32);
        fEnemy.add(anm);
        //Parts
        //SpritePart sprite = new SpritePart("enemy/enemyright/right_01.png", 32, 32);
        //fEnemy.add(sprite);
        WeaponPart wpn = new WeaponPart(damage, range, speed);
        fEnemy.add(wpn);
        MovingPart mov = new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed);
        fEnemy.add(mov);
        PositionPart pos = new PositionPart(x, y, radians);
        fEnemy.add(pos);
        LifePart lif = new LifePart(life);
        fEnemy.add(lif);

        return fEnemy;
    }

}
