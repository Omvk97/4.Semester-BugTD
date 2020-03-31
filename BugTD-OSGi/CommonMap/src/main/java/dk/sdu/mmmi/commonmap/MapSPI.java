package dk.sdu.mmmi.commonmap;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.ArrayList;
import java.util.List;

public interface MapSPI {

    Tile[][] getTiles();

    Tile getClosestTile(float x, float y);

    ArrayList<Tile> getTilesEntityIsOn(Entity e);

    Tile getTileXAndY(Tile t);

    boolean checkIfTileIsOccupied(Tile t, List<Entity> ignoreThese);

    void loadFile(String filepath);
}
