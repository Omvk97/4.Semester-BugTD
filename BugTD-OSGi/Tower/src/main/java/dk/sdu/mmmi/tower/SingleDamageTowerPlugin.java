package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commontower.Tower;

public class SingleDamageTowerPlugin implements IGamePluginService {
    
    public final static String BASIC_TOWER_PATH = "towers/basictower.png";
    public final static String BASIC_TOWER_PREVIEW_LEGAL_PATH = "towers/basictower_preview_legal.png";
    public final static String BASIC_TOWER_PREVIEW_ILLEGAL_PATH = "towers/basictower_preview_illegal.png";

    @Override
    public void start(GameData gameData, World world) {
        int width = 32;
        int height = 32;
        int layer = 1;
        
        Entity basicTower = new Entity();
        basicTower.add(new SpritePart(BASIC_TOWER_PATH, width, height, layer));
        world.addEntity(basicTower);
        
        Entity basicTowerPreviewLegal = new Entity();
        basicTowerPreviewLegal.add(new SpritePart(BASIC_TOWER_PREVIEW_LEGAL_PATH, width, height, layer));
        world.addEntity(basicTowerPreviewLegal);
        
        Entity basicTowerPreviewIllegal = new Entity();
        basicTowerPreviewIllegal.add(new SpritePart(BASIC_TOWER_PREVIEW_ILLEGAL_PATH, width, height, layer));
        world.addEntity(basicTowerPreviewIllegal);
        
        // TODO: Find better way to load initial sprite assets
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tower : world.getEntities(Tower.class)) {
            world.removeEntity(tower);
        }
    }
}
