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
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.TileSizes;
import dk.sdu.mmmi.commontower.Queen;
import dk.sdu.mmmi.commontower.Tower;
import dk.sdu.mmmi.commontower.TowerPreview;

public class TowerPlugin implements IGamePluginService {
    
    public final static String BASIC_TOWER_PATH = "towers/basictower.png";
    public final static String BASIC_TOWER_PREVIEW_LEGAL_PATH = "towers/basictower_preview_legal.png";
    public final static String BASIC_TOWER_PREVIEW_ILLEGAL_PATH = "towers/basictower_preview_illegal.png";
    public final static String QUEEN_PATH = "towers/queen.png";
    
    private MapSPI map;
    private Queen queen;

    @Override
    public void start(GameData gameData, World world) {
        int width = 32;
        int height = 32;
        int layer = 1;
        
        // TODO: Find better way to load initial sprite assets
        Entity basicTower = new Entity();
        basicTower.add(new SpritePart(BASIC_TOWER_PATH, width, height, layer));
        world.addEntity(basicTower);
        
        Entity basicTowerPreviewLegal = new Entity();
        basicTowerPreviewLegal.add(new SpritePart(BASIC_TOWER_PREVIEW_LEGAL_PATH, width, height, layer));
        world.addEntity(basicTowerPreviewLegal);
        
        Entity basicTowerPreviewIllegal = new Entity();
        basicTowerPreviewIllegal.add(new SpritePart(BASIC_TOWER_PREVIEW_ILLEGAL_PATH, width, height, layer));
        world.addEntity(basicTowerPreviewIllegal);
        
        PositionPart pos = new PositionPart(gameData.getDisplayWidth() / 2 - 2*TileSizes.GRASS_WIDTH, gameData.getDisplayHeight() / 10, 0);
        LifePart life = new LifePart(100);
        CollisionPart collision = new CollisionPart(4*TileSizes.GRASS_WIDTH, 4*TileSizes.GRASS_HEIGHT);
        WeaponPart weapon = new WeaponPart(10, 10, 1);
        SpritePart sprite = new SpritePart(QUEEN_PATH, 4*TileSizes.GRASS_WIDTH, 4*TileSizes.GRASS_HEIGHT, layer);
        queen = new Queen(pos, life, collision, weapon, sprite);
        world.addEntity(queen);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tower : world.getEntities(Tower.class)) {
            world.removeEntity(tower);
        }
        for (Entity preview : world.getEntities(TowerPreview.class)) {
            world.removeEntity(preview);
        }
        for (Entity queen : world.getEntities(Queen.class)) {
            world.removeEntity(queen);
        }
    }
    
    public void setMapSPI(MapSPI spi) {
        this.map = spi;
        
        map.fitEntityToMap(queen);      // This is pretty bull. Ehm will probably get fixed naturally in the future ¯\_(?)_/¯
    }
}
