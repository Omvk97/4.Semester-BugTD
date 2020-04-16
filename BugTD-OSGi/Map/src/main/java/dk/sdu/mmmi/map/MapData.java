package dk.sdu.mmmi.map;

import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.commonmap.Tile;

import java.util.ArrayList;
import java.util.Scanner;

public class MapData {
    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    private final int TILE_SIZE = 16;

    public MapData(Scanner sc) {
        initFromScanner(sc);
    }

    public ArrayList<ArrayList<Tile>> getTiles() {
        return tiles;
    }

    private void initFromScanner(Scanner sc) {
        DataType currentType = DataType.Default;
        String dataChunk = "";

        while (sc.hasNextLine()) {

            String nextLine = sc.nextLine();

            // New section in data
            if (nextLine.contains("@")) {
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
                if (nextLine.length() == 0) continue;
                dataChunk += nextLine + "\n";
            }
        }
    }

    public void addTilesToWorld(World world) {
        for (int i = 0; i < tiles.size(); i++) {
            ArrayList<Tile> row =tiles.get(i);
            for (Tile tile : row) {
                world.addEntity(tile);
            }
        }
    }

    private void processDataChunk(String dataChunk, DataType currentType) {
        try (Scanner sc = new Scanner(dataChunk)) {
            switch (currentType) {
                case Map:
                    int i = 0;
                    while (sc.hasNextLine()) {
                        String nextLine = sc.nextLine();
                        if (nextLine.length() < 1) continue;
                        String[] chars = nextLine.split("");
                        ArrayList<Tile> row = new ArrayList<>();
                        for (int j = 0; j < chars.length; j++) {
                            int current = Integer.parseInt(chars[i]);

                            boolean walkable = false;
                            SpritePart tileSpritePart = null;

                            if (current == TileType.Grass.getNumVal()) {
                                tileSpritePart = new SpritePart("map/grass_16x16.png", TILE_SIZE, TILE_SIZE, 0);
                                walkable = true;
                            } else if (current == TileType.Dirt.getNumVal()) {
                                tileSpritePart = new SpritePart("map/dirt_16x16.png", TILE_SIZE, TILE_SIZE, 0);
                            }

                            PositionPart tilePositionPart = new PositionPart(j * TILE_SIZE, i * TILE_SIZE, Math.PI / 2);
                            CollisionPart collisionPart = new CollisionPart(TILE_SIZE, TILE_SIZE);
                            Tile tile = new Tile(walkable, tileSpritePart, tilePositionPart);
                            tile.add(collisionPart);
                            row.add(tile);
                        }
                        tiles.add(row);
                        i++;
                    }
                case Enemies:
                    System.out.println("Enemies");
                    System.out.println(dataChunk);
                    break;
                case Default:
                    return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    enum DataType {
        Default,
        Map,
        Enemies,
        End
    }

    public enum TileType {
        Grass(0), Dirt(1);

        private int numVal;

        TileType(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }
}
