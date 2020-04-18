package dk.sdu.mmmi.queen;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.TileSizes;
import dk.sdu.mmmi.commontower.Queen;

public class QueenPlugin implements IGamePluginService {
    
    public final static String QUEEN_PATH = "towers/queen.png";

    @Override
    public void start(GameData gameData, World world) {
        PositionPart pos = new PositionPart(gameData.getDisplayWidth() / 2 - 2*TileSizes.GRASS_WIDTH, gameData.getDisplayHeight() / 10, 0);
        LifePart life = new LifePart(100);
        CollisionPart collision = new CollisionPart(4*TileSizes.GRASS_WIDTH, 4*TileSizes.GRASS_HEIGHT);
        WeaponPart weapon = new WeaponPart(10, 10, 1);
        SpritePart sprite = new SpritePart(QUEEN_PATH, 4*TileSizes.GRASS_WIDTH, 4*TileSizes.GRASS_HEIGHT, 1);
        Entity queen = new Queen(pos, life, collision, weapon, sprite);
        world.addEntity(queen);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity queen : world.getEntities(Queen.class)) {
            world.removeEntity(queen);
        }
    }
}
