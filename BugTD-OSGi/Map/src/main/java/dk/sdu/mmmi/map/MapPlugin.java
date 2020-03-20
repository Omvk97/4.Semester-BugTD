/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;

/**
 *
 * @author oliver
 */
public class MapPlugin implements IGamePluginService, MapSPI {
    
    private Tile[][] tiles;
    
    @Override
    public void start(GameData gameData, World world) {
        // 16 rows length, 16 wide
        tiles = new Tile[16][16];
        
        int rowsWithGrassInSides = 3;
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                
                boolean walkable = true;
                if (j <= rowsWithGrassInSides - 1 || j >= tiles[0].length - rowsWithGrassInSides) {
                    walkable = false;
                }
                
                tiles[i][j] = new Tile(walkable);
            }
        }
        
        
    }

    @Override
    public void stop(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadFile(String filepath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tile[][] getTiles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
