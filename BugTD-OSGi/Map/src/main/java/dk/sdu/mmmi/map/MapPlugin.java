package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.TileSizes;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapPlugin implements IGamePluginService, MapSPI {

    private World world;
    private MapData mapData;

    @Override
    public void start(GameData gameData, World world) {
        this.world = world;
        initMapDataFromFile("/levels/level01.buggydata");
        mapData.addTilesToWorld(world);
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
        return mapData.getTiles();
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

        Tile[][] tiles = mapData.getTiles();

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

    }

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

    @Override
    public Tile getTileInDirection(Tile tile, Direction direction) throws ArrayIndexOutOfBoundsException {
        Tile[][] tiles = mapData.getTiles();

        PositionPart positionpart = tile.getPart(PositionPart.class);
        int x = (int) positionpart.getX() / MapData.TILE_SIZE;
        int y = (int) positionpart.getY() / MapData.TILE_SIZE;
        if (direction == Direction.UP) {
            return tiles[y-1][x];
        }
        if (direction == Direction.RIGHT) {
            return tiles[y][x+1];
        }
        if (direction == Direction.DOWN) {
            return tiles[y+1][x];
        }
        if (direction == Direction.LEFT) {
            return tiles[y][x-1];
        }
        throw new ArrayIndexOutOfBoundsException("Yolo");
    }

    private void initMapDataFromFile(String path) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (Scanner sc = new Scanner(new InputStreamReader(classLoader.getResource(path).openStream()))) {
            mapData = new MapData(sc);
        } catch (Exception ex) {
            System.out.println("Exception caught while reading file");
            System.out.println(ex);
        }
    }

}

