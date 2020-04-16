package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonenemy.Enemy;

/**
 *
 * @author marcu
 */
public class FlyingEnemy implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity fEnemy = new Enemy();
        //attributes
        float damage = 10;
        float range = 50;
        float speed = 10;
        float deacceleration = 100;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = 700;
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
        PositionPart pos  = new PositionPart(x, y, radians);
        fEnemy.add(pos);
        LifePart lif = new LifePart(life);
        fEnemy.add(lif);
        world.addEntity(fEnemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }

}
