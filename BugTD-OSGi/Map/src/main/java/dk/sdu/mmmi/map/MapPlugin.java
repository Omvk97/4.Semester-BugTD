package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.TileSizes;

import java.util.ArrayList;
import java.util.List;

public class MapPlugin implements IGamePluginService, MapSPI {

    private Tile[][] tiles;
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        this.world = world;

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
        PositionPart posPart = entity.getPart(PositionPart.class);
        SpritePart spritePart = entity.getPart(SpritePart.class);

        if (posPart == null || spritePart == null) {
            return new ArrayList<>();
        }

        ArrayList<Tile> overlappingTiles = new ArrayList<>();
        int tileNumberFromLeft = (int) posPart.getX() / TileSizes.GRASS_WIDTH;
        int tileNumberFromBottom = (int) posPart.getY() / TileSizes.GRASS_WIDTH;
        int entityWidthInTiles = (int) spritePart.getWidth() / TileSizes.GRASS_WIDTH;
        int entityHeightInTiles = (int) spritePart.getHeight() / TileSizes.GRASS_WIDTH;

        for (int i = tileNumberFromLeft; i < tileNumberFromLeft + entityWidthInTiles; i++) {
            for (int j = tileNumberFromBottom; j < tileNumberFromBottom + entityHeightInTiles; j++) {
                try {
                    overlappingTiles.add(tiles[j][i]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Do nothing
                }
            }
        }

        return overlappingTiles;

//        for (int i = 0; i < tiles.length; i ++) {
//            for (int j = 0; j < tiles[i].length; j++) {
//                Tile tile = tiles[i][j];
//                if (collides(entity, tile)) {
//                    overlappingTiles.add(tile);
//                }
//            }
//        }
//        return overlappingTiles;
    }


//    private boolean collides(Entity e1, Entity e2) {
//        PositionPart p1 = e1.getPart(PositionPart.class);
//        SpritePart s1 = e1.getPart(SpritePart.class);
//        float x = p1.getX();
//        float y = p1.getY();
//        float width = s1.getWidth();
//        float height = s1.getHeight();
//
//        PositionPart p2 = e2.getPart(PositionPart.class);
//        SpritePart s2 = e2.getPart(SpritePart.class);
//        float otherX = p2.getX();
//        float otherY = p2.getY();
//        float otherWidth = s2.getWidth();
//        float otherHeight = s2.getHeight();
//
//        if (collidesOnAxis(x, x + width, otherX, otherX + otherWidth)
//                && collidesOnAxis(y, y + height, otherY, otherY + otherHeight)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean collidesOnAxis(float start1, float end1, float start2, float end2) {
//        return (start1 <= end2 && end1 > start2);
//    }
    @Override
    public Tile getTileXAndY(Tile tile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkIfTileIsOccupied(Tile t, List<Entity> ignoreThese) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Tile || ignoreThese.contains(entity)) {
                continue;
            }

            if (getTilesEntityIsOn(entity).contains(t)) {
                return true;
            }
        }
        return false;
    }
}

