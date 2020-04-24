package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyType;

public class GroundEnemy implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        //attributes
        float weaponDamage = 50;
        float weaponRange = 50;
        float weaponSpeed = 10;
        float speedPerMovement = 1;
        float x = 400;
        float y = 400;
        float radians = 3.1415f / 2;
        int life = 100;

        Entity gEnemy = new Enemy(EnemyType.GROUND,
                new WeaponPart(weaponDamage, weaponRange, weaponSpeed),
                new AnimationPart("texturesprites/enemy/enemyup.atlas", 16, 16, 0),
                new PositionPart(x + 16, y + 16, radians),
                new PreciseMovingPart(speedPerMovement),
                new LifePart(life));

        world.addEntity(gEnemy);

        Entity enemy2 = new Enemy(EnemyType.GROUND,
                new WeaponPart(weaponDamage, weaponRange, weaponSpeed),
                new AnimationPart("texturesprites/enemy/enemyup.atlas", 16, 16, 0),
                new PositionPart(x + 16, y + 16, radians),
                new PreciseMovingPart(speedPerMovement),
                new LifePart(life)
        );

        world.addEntity(enemy2);

    }

    @Override
    public void stop(GameData gameData, World world) {
        // TODO - Only remove ground enemies
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }

}
