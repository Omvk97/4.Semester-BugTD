/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;

import java.util.ArrayList;

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
        final int TILE_SIZE = 16;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                
                boolean walkable = true;
                
                // Determine if tile is environment or path tile
                if (j <= rowsWithGrassInSides - 1 || j >= tiles[0].length - rowsWithGrassInSides) {
                    walkable = false;
                }
                
                SpritePart tileSpritePart = new SpritePart(walkable ? "map/grass_16x16.png" : "map/dirt_16x16.png", TILE_SIZE, TILE_SIZE, 0);
                
                PositionPart tilePositionPart = new PositionPart(j * TILE_SIZE, i * TILE_SIZE, Math.PI / 2);
                CollisionPart collisionPart = new CollisionPart(TILE_SIZE, TILE_SIZE);
                Tile tile = new Tile(walkable, tileSpritePart, tilePositionPart);
                tile.add(collisionPart);
                tiles[i][j] = tile;
                world.addEntity(tile);
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tile : world.getEntities(Tile.class)) {
            world.removeEntity(tile);
        }
    }

    @Override
    public void loadFile(String filepath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    public Tile getClosestTile(float x, float y) {
        return null;
    }

    @Override
    public ArrayList<Tile> getTilesEntityIsOn(Entity entity) {
        ArrayList<Tile> overlappingTiles = new ArrayList<>();

        for (int i = 0; i < tiles.length; i ++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                if (CollisionPart.collides(entity, tile)) {
                    overlappingTiles.add(tile);
                }
            }
        }
        return overlappingTiles;
    }


    @Override
    public Tile getTileXAndY(Tile tile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean CheckIfTileIsOccupied(Tile t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
