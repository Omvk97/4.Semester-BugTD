package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.*;

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
        loadFile("/levels/level01.buggydata");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity tile : world.getEntities(Tile.class)) {
            world.removeEntity(tile);
        }
    }

    @Override
    public void loadFile(String filepath) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (Scanner sc = new Scanner(new InputStreamReader(classLoader.getResource(filepath).openStream()))) {
            mapData = new MapData(16, sc);
            mapData.addTilesToWorld(world);
            mapData.addQueenToWorld(world);
        } catch (Exception ex) {
            System.out.println("Exception caught while reading map file [" + filepath + "]");
            System.out.println(ex);
        }
    }

    @Override
    public ArrayList<MapWave> getMapWaves() {
        return mapData.getWaves();
    }

    @Override
    public float getEnemySpawnY() {
        return mapData.getEnemySpawnPoint().y;
    }

    @Override
    public float getEnemySpawnX() {
        return mapData.getEnemySpawnPoint().x;
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
        AnimationPart animationPart = entity.getPart(AnimationPart.class);
        
        // Entitity has to have a position part, and either a spritePart or an animationpart
        if (posPart == null || (spritePart == null && animationPart == null)) {
            return new ArrayList<>();
        }

        ArrayList<Tile> overlappingTiles = new ArrayList<>();
        int tileNumberFromLeft = (int) posPart.getX() / TileSizes.GRASS_WIDTH;
        int tileNumberFromBottom = (int) posPart.getY() / TileSizes.GRASS_WIDTH;
        int entityWidthInTiles = (int) (spritePart == null ? animationPart.getWidth() : spritePart.getWidth()) / TileSizes.GRASS_WIDTH;
        int entityHeightInTiles = (int) (spritePart == null ? animationPart.getHeight() : spritePart.getHeight()) / TileSizes.GRASS_WIDTH;

        for (int i = tileNumberFromLeft; i < tileNumberFromLeft + entityWidthInTiles; i++) {
            for (int j = tileNumberFromBottom; j < tileNumberFromBottom + entityHeightInTiles; j++) {
                try {
                    overlappingTiles.add(mapData.getTiles()[j][i]);
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
    public <E extends Entity> boolean checkIfTileIsOccupied(Tile t, Class<E>... ignoreTheseClasses) {
        for (Entity entity : world.getEntities()) {
            boolean entityShouldBeIgnored = false;
            for (Class<E> entityType : ignoreTheseClasses) {
                if (entityType.equals(entity.getClass())) {
                    entityShouldBeIgnored = true;
                }
            }

            if (entity instanceof Tile || entityShouldBeIgnored) {
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
        int x = (int) positionpart.getX() / mapData.getTileSize();
        int y = (int) positionpart.getY() / mapData.getTileSize();
        if (direction == Direction.UP) {
            return tiles[y - 1][x];
        }
        if (direction == Direction.RIGHT) {
            return tiles[y][x + 1];
        }
        if (direction == Direction.DOWN) {
            return tiles[y + 1][x];
        }
        if (direction == Direction.LEFT) {
            return tiles[y][x - 1];
        }
        throw new ArrayIndexOutOfBoundsException("Yolo");
    }

    @Override
    public void fitEntityToMap(Entity e) {
        PositionPart pos = e.getPart(PositionPart.class);
        pos.setX(roundDown(pos.getX(), TileSizes.GRASS_WIDTH));
        pos.setY(roundDown(pos.getY(), TileSizes.GRASS_HEIGHT));
    }

    private int roundDown(double number, double place) {
        double result = number / place;
        result = Math.floor(result);
        result *= place;
        return (int) result;
    }
}
