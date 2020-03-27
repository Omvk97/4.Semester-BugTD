/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.dummyplayer;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.MapSPI;

/**
 *
 * @author oliver
 */
public class TestService implements IEntityProcessingService {
    
    private MapSPI mapSPI;

    @Override
    public void process(GameData gameData, World world) {
        System.out.println(mapSPI.getTiles()[0][0].isWalkable());
    }
    
    public void setMapSPI(MapSPI map) {
        System.out.println("Am i called?");
        this.mapSPI = map;
    }
    
    public void removeMapSPI(MapSPI map) {
        this.mapSPI = null;
    }  
    
}
