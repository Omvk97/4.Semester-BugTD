package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonenemy.Enemy;

/**
 *
 * @author marcu
 */
public class GroundEnemy implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity gEnemy = new Enemy();
        //attributes
        float damage = 10;
        float range = 50;
        float speed = 10;
        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 3;
        float radians = 0;
        int life = 100;

        //Parts
        SpritePart sprite = new SpritePart("one-anty-boi.png", 32, 32);
        gEnemy.add(sprite);
        WeaponPart wpn = new WeaponPart(damage, range, speed);
        gEnemy.add(wpn);
        MovingPart mov = new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed);
        gEnemy.add(mov);
        PositionPart pos = new PositionPart(x, y, radians);
        gEnemy.add(pos);
        LifePart lif = new LifePart(life);
        gEnemy.add(lif);
        world.addEntity(gEnemy);

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }

}
