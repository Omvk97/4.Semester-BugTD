package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.commonmap.MapWave;
import dk.sdu.mmmi.commonmap.Tile;

import java.util.ArrayList;
import java.util.Scanner;

public class MapData {
    private Tile[][] tiles;
    private ArrayList<MapWave> waves;
    private int tileSize = 16;

    public MapData(int tileSize) {
        this.tileSize = tileSize;
        tiles = new Tile[0][0];
        waves = new ArrayList<>();
    }

    public MapData(int tileSize, Scanner sc) {
        this(tileSize);
        initFromScanner(sc);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public ArrayList<MapWave> getWaves() {
        return waves;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void addTilesToWorld(World world) {
        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];
            for (Tile tile : row) {
                world.addEntity(tile);
            }
        }
    }

    private void initFromScanner(Scanner sc) {
        DataType currentType = DataType.Default;
        String dataChunk = "";

        while (sc.hasNextLine()) {

            String nextLine = sc.nextLine();
            if (nextLine.length() == 0) continue;

            // New section in data
            if (nextLine.charAt(0) == '@') {
                // Process previous data
                if (dataChunk.length() > 0) {
                    processDataChunk(dataChunk, currentType);
                    dataChunk = "";
                }

                // Figure out which type of data is incoming
                for (DataType type : DataType.values()) {
                    if (nextLine.contains(type.toString())) {
                        currentType = type;
                    }
                }

            } else {
                dataChunk += nextLine + "\n";
            }
        }
    }

    private void processDataChunk(String dataChunk, DataType currentType) {
        try (Scanner sc = new Scanner(dataChunk)) {

            ArrayList<String> lines = new ArrayList<>();

            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if (nextLine.length() < 1) continue;
                lines.add(nextLine);
            }

            switch (currentType) {
                case Map:
                    processMapChunk(lines);
                    break;
                case Waves:
                    processWavesChunk(lines);
                    break;
                case Default:
                    return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processWavesChunk(ArrayList<String> lines) {
        int waveNumber = 0;
        for (String line : lines) {
            waveNumber++;
            String[] splitLine = line.split("\\*");
            String enemyType = splitLine[0];
            int enemyCount = Integer.parseInt(splitLine[1]);
            MapWave wave = new MapWave(enemyType, enemyCount, waveNumber);
            System.out.println(wave);
            waves.add(wave);

        }
    }

    private void processMapChunk(ArrayList<String> lines) {
        tiles = new Tile[lines.size()][];
        int y = 0;
        for (String nextLine : lines) {
            char[] chars = nextLine.toCharArray();

            Tile[] row = new Tile[chars.length];

            //TODO: Map loads upside down due to way the LibGDX renders coordinates.
            for (int x = 0; x < chars.length; x++) {
                int current = Character.getNumericValue(chars[x]);
                boolean walkable = false;
                SpritePart tileSpritePart = null;

                if (current == TileType.Grass.getNumVal()) {
                    tileSpritePart = new SpritePart("map/grass_16x16.png", tileSize, tileSize, 0);
                    walkable = true;
                } else if (current == TileType.Dirt.getNumVal()) {
                    tileSpritePart = new SpritePart("map/dirt_16x16.png", tileSize, tileSize, 0);
                }
                PositionPart tilePositionPart = new PositionPart(x * tileSize, y * tileSize, Math.PI / 2);
                CollisionPart collisionPart = new CollisionPart(tileSize, tileSize);
                Tile tile = new Tile(walkable, tileSpritePart, tilePositionPart);
                tile.add(collisionPart);
                row[x] = tile;
            }
            tiles[y] = row;
            y++;
        }
    }

    private enum DataType {
        Default,
        Map,
        Waves,
        End
    }

    private enum TileType {
        Grass(1), Dirt(0);

        private int numVal;

        TileType(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }
}
