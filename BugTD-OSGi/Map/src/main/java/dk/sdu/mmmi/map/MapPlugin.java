/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.TileSizes;

/**
 *
 * @author oliver
 */
public class MapPlugin implements IGamePluginService, MapSPI {
    
    private Tile[][] tiles;
    
    @Override
    public void start(GameData gameData, World world) {
        // 52 rows length, 52 wide. Grass tiles are bigger in size and should therefore not take up as much space.
        tiles = new Tile[52][52];
        
        // The first three tiles and the last three tiles will be tiles with grass (environment)
        int rowsWithGrassInSides = 3;
        
        // TODO - Make it so environment can have different sizes than path. The problem is that when environment is bigger, there should not be places as many environment tiles as path tiles
        // which means the length becomes very long.
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                
                boolean walkable = true;
                
                // Determine if tile is environment or path tile
                if (j <= rowsWithGrassInSides - 1 || j >= tiles[0].length - rowsWithGrassInSides) {
                    walkable = false;
                }
                
                // Find x position of the tile to the left of the current one (if it exists)
                int precessorTilePositionX = (int) (j == 0 ? 0 : ((PositionPart) tiles[i][j - 1].getPart(PositionPart.class)).getX());
                // finx y position of the tile above the current one (if it exists)
                int precessorTilePositionY = (int) (i == 0 ? 0 : ((PositionPart) tiles[i - 1][j].getPart(PositionPart.class)).getY());
                
                int x = precessorTilePositionX + (walkable ? TileSizes.DIRT_WIDTH : TileSizes.GRASS_WIDTH);
                
                int y = precessorTilePositionY + (walkable ? TileSizes.DIRT_HEIGHT : TileSizes.GRASS_HEIGHT);
                
                
                PositionPart tilePositionPart = new PositionPart(x, y, Math.PI / 2);
                Tile tile = new Tile(walkable, tilePositionPart);
                tiles[i][j] = tile;
                world.addEntity(tile);
            }
        }
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                PositionPart test = tiles[i][j].getPart(PositionPart.class);
                System.out.println(
                        "I : " + i + " J : " + j + "\n" +
                        "Y : " + test.getY() + " X : " + test.getX() + "\n");
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
