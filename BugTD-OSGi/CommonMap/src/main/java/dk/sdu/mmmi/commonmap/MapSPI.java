package dk.sdu.mmmi.commonmap;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.ArrayList;
import java.util.List;

public interface MapSPI {

    void loadFile(String filepath);


    /* MAP STATE */

    float getEnemySpawnY();

    float getEnemySpawnX();

    ArrayList<MapWave> getMapWaves();

    Tile[][] getTiles();

    void setQueenID(String queenID);

    Entity getQueen();


    /* UTILITY FUNCTIONS */

    Tile getClosestTile(float x, float y);

    ArrayList<Tile> getTilesEntityIsOn(Entity e);

    Tile getTileXAndY(Tile t);

    boolean checkIfTileIsOccupied(Tile t, List<Entity> ignoreThese);

    public <E extends Entity> boolean checkIfTileIsOccupied(Tile t, Class<E>... ignoreTheseClasses);

    Tile getTileInDirection(Tile tile, Direction direction) throws ArrayIndexOutOfBoundsException;

    void fitEntityToMap(Entity e);
    
    float distance(Entity e1, Entity e2);

}
