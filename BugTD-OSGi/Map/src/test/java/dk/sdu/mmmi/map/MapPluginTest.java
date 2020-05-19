package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.commontower.Queen;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MapPluginTest {

    MapPlugin mapPlugin;
    GameData mockedGameData;
    World world;

    @Before
    public void setupWorld() {
        world = new World();
    }

    @Before
    public void setupGameData() {
        mockedGameData = mock(GameData.class);
        // Make sure we load level 1, just for consistent testing
        when(mockedGameData.getDifficulty()).thenReturn(1);
    }

    @Before
    public void setupMapPlugin() {
        mapPlugin = new MapPlugin();
        mapPlugin.start(mockedGameData, world);
    }

    // Unit testing of MapSPI method: getTilesEntityIsOn
    @Test
    public void testGetTilesEntityIsOn() {
        Entity mockedEntity = mock(Entity.class);

        // Setup mocked entity dimensions
        float entityHeight = 50;
        float entityWidth = 50;

        SpritePart mockedSpritePart = mock(SpritePart.class);
        when(mockedEntity.getPart(SpritePart.class)).thenReturn(mockedSpritePart);
        when(mockedSpritePart.getWidth()).thenReturn(entityWidth);
        when(mockedSpritePart.getHeight()).thenReturn(entityHeight);


        // Setup mocked entity position
        float entityX = 0;
        float entityY = 0;

        PositionPart mockedPositionpart = mock(PositionPart.class);
        when(mockedEntity.getPart(PositionPart.class)).thenReturn(mockedPositionpart);
        when(mockedPositionpart.getX()).thenReturn(entityX);
        when(mockedPositionpart.getX()).thenReturn(entityY);


        // Manually calculate number of tiles mockedEntity fully covers
        int tileSize = mapPlugin.getMapData().getTileSize();
        int collidingTilesX = Math.round(entityWidth / tileSize);
        int collidingTilesY = Math.round(entityHeight / tileSize);
        int collidingTiles = collidingTilesX * collidingTilesY;

        assert(mapPlugin.getTilesEntityIsOn(mockedEntity).size() == collidingTiles);
    }

    @Test
    public void mapAndQueenIntegration() {
        // Get queen from world
        List<Entity> queensInGame = world.getEntities(Queen.class);
        assert(queensInGame.size() > 0);
        Queen queen = (Queen) queensInGame.get(0);

        // Map can create a queen from the loaded text file
        assert(queen != null);

        // Map adds the created queen to the world's list of entities
        assert(world.getEntity(queen.getID()) != null);

        mapPlugin.stop(mockedGameData, world);

        // Map can remove queen from the world.
        assert(world.getEntity(queen.getID()) == null);
    }
}
