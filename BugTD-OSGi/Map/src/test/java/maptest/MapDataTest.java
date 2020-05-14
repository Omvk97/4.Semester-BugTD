package maptest;

import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.map.MapData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MapDataTest {

    World world;
    MapData mapData;

    @Before
    public void setup() {
        world = new World();
    }

    @Before
    public void canLoadFromFile() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Scanner sc = new Scanner(new InputStreamReader(classLoader.getResource("levels/level01.buggydata").openStream()));
        mapData = new MapData(16, sc, world);
    }

    @Test
    public void loadsQueen() {
        assert(mapData.getQueen() != null);
    }

    @Test
    public void loadsTiles() {
        assert(mapData.getTiles().length > 0);
    }

    @Test
    public void loadsEnemySpawner() {
        assert(mapData.getEnemySpawnPoint() != null);
    }

    @Test
    public void loadsWaves() {
        assert(mapData.getWaves().size() > 0);
    }

}
