package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.*;
import dk.sdu.mmmi.cbse.common.events.QueenSpawnedEvent;
import dk.sdu.mmmi.commonmap.MapWave;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.osgienemyspawner.EnemySpawnPoint;

import java.util.ArrayList;
import java.util.Scanner;


public class MapData {
    private Tile[][] tiles;
    private ArrayList<MapWave> waves;
    private int tileSize;
    private EnemySpawnPoint enemySpawnPoint;
    private GameData gameData;

    public MapData(int tileSize, Scanner sc, World world, GameData gameData) {
        this.tileSize = tileSize;
        tiles = new Tile[0][0];
        waves = new ArrayList<>();
        this.gameData = gameData;
        initFromScanner(sc);

        addTilesToWorld(world);
    }

    public EnemySpawnPoint getEnemySpawnPoint() {
        return enemySpawnPoint;
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

    private void addTilesToWorld(World world) {
        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];
            for (Tile tile : row) {
                world.addEntity(tile);
            }
        }
    }

    private enum DataType {
        Default,
        Tiles,
        Waves,
        Queen,
        EnemySpawn,
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
                case Tiles:
                    processTilesChunk(lines);
                    break;
                case Waves:
                    processWavesChunk(lines);
                    break;
                case Queen:
                    processQueenChunk(lines);
                    break;
                case EnemySpawn:
                    processEnemySpawnChunk(lines);
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
            String type = "";
            int amount = 0;
            int life = 0;
            String[] splitLine = line.split(";");
            for (String split : splitLine) {
                String key = split.split("=")[0];
                String value = split.split("=")[1];
                switch (key) {
                    case "Type":
                        type = value;
                        break;
                    case "Amount":
                        amount = Integer.parseInt(value);
                        break;
                    case "Life":
                        life = Integer.parseInt(value);
                        break;
                    default:
                        break;
                }
            }
            MapWave wave = new MapWave(type, amount, waveNumber, life);
            waves.add(wave);

        }
    }

    private void processTilesChunk(ArrayList<String> lines) {
        tiles = new Tile[lines.size()][];
        int y = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            String nextLine = lines.get(i);
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

    protected void processQueenChunk(ArrayList<String> lines) {
        QueenSpawnedEvent queenSpawnedEvent = new QueenSpawnedEvent(null);

        for (String line : lines) {
            String type = line.split("=")[0];
            String value = line.split("=")[1];
            switch(type) {
                case "X":
                    queenSpawnedEvent.setX(Float.parseFloat(value));
                    break;
                case "Y":
                    queenSpawnedEvent.setY(Float.parseFloat(value));
                    break;
                case "LIFE":
                    queenSpawnedEvent.setLife(Float.parseFloat(value));
                    break;
                case "DAMAGE":
                    queenSpawnedEvent.setDamage(Float.parseFloat(value));
                    break;
                case "RANGE":
                    queenSpawnedEvent.setRange(Float.parseFloat(value));
                    break;
                case "ATTACKSPEED":
                    queenSpawnedEvent.setAttackSpeed(Float.parseFloat(value));
                    break;
                default:
                    return;
            }
        }
        gameData.addEvent(queenSpawnedEvent);
    }

    private void processEnemySpawnChunk(ArrayList<String> lines) {
        this.enemySpawnPoint = new EnemySpawnPoint();
        for (String line : lines) {
            String type = line.split("=")[0];
            String value = line.split("=")[1];
            switch(type) {
                case "X":
                    this.enemySpawnPoint.x = Float.parseFloat(value);
                    break;
                case "Y":
                    this.enemySpawnPoint.y = Float.parseFloat(value);
                    break;
                default:
                    return;
            }
        }
    }
}
