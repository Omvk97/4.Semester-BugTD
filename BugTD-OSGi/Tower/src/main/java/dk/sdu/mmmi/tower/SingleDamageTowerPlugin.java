package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commontower.Tower;

public class SingleDamageTowerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 0;
        PositionPart pos = new PositionPart(x, y, radians);

        int hp = 100;
        LifePart life = new LifePart(hp);

        CollisionPart colli = new CollisionPart();

        float damage = 10;
        float range = 50;
        float speed = 10;
        WeaponPart wpn = new WeaponPart(damage, range, speed);

        Tower tower = new Tower(pos, life, colli, wpn);
        world.addEntity(tower);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tower : world.getEntities(Tower.class)) {
            world.removeEntity(tower);
        }
    }
}
