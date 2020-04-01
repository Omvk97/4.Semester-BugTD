/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.dummyplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;

/**
 *
 * @author oliver
 */
public class TestService implements IEntityProcessingService {
    
    private MapSPI mapSPI;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities(DummyPlayer.class)) {
            PositionPart p = e.getPart(PositionPart.class);
            SpritePart s = e.getPart(SpritePart.class);
//            System.out.format("Dummy player is colliding with %d tiles\n", mapSPI.getTilesEntityIsOn(e).size());
        }
    }
    
    public void setMapSPI(MapSPI map) {
        System.out.println("Am i called?");
        this.mapSPI = map;
    }
    
    public void removeMapSPI(MapSPI map) {
        this.mapSPI = null;
    }  
    
}
