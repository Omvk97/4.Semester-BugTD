package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commontower.Tower;

public class SingleDamageTowerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        float x = 300;
        float y = 300;
        float radians = 0;
        PositionPart pos = new PositionPart(x, y, radians);

        int hp = 100;
        LifePart life = new LifePart(hp);

        CollisionPart colli = new CollisionPart();

        float damage = 10;
        float range = 50;
        float speed = 1;
        WeaponPart wpn = new WeaponPart(damage, range, speed);
        
        int width = 32;
        int height = 32;
        int layer = 1;
        SpritePart sprt = new SpritePart("basictower.png", width, height, layer);

        Tower tower = new Tower(pos, life, colli, wpn, sprt);
        world.addEntity(tower);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tower : world.getEntities(Tower.class)) {
            world.removeEntity(tower);
        }
    }
}
