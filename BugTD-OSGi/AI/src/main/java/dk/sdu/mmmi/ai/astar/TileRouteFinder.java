/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author oliver
 */
public class TileRouteFinder {
    private Graph<MapTile> tilesGraph;

    private RouteFinder<MapTile> routeFinder;
    private MapSPI map;

    public void test(Tile[][] mapTiles) throws Exception {
        Set<MapTile> tiles = new HashSet<MapTile>();
        Map<String, Set<String>> connections = getConnections();
        for (int i = 0; i < mapTiles.length; i++) {
            for (int j = 0; j < mapTiles[i].length; j++) {
                Tile tile = mapTiles[i][j];
                tiles.add(new MapTile(tile.getID(), tile.getX(), tile.getY()));
            }
        }
        
        tilesGraph = new Graph<>(tiles, connections);
        routeFinder = new RouteFinder<>(tilesGraph, new TilePathCostScorer(), new QueenHeuristicScorer());
        
        // TODO - Insert enemy position and queen position from tiles
        List<MapTile> route = routeFinder.findRoute(tilesGraph.getNode("1"), tilesGraph.getNode("7"));
 
        System.out.println(route.stream().map(MapTile::getPosition).collect(Collectors.toList()));
    }

    private Map<String, Set<String>> getConnections(Tile tile, Tile[][] mapTiles) {
        HashMap<String, Set<String>> connections = new HashMap<>();
        for (int y = 0; y < mapTiles.length; y++) {
            for (int x = 0; x < mapTiles[y].length; x++) {
                HashSet neighbors = new HashSet<String>();
                for (Direction direction : Direction.values()) {
                    try {
                        Tile neighbor = map.getTileInDirection(tile, direction);
                        neighbors.add(neighbor);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Don't add
                    }
                }
                connections.put(tile.getID(), neighbors);
            }
        }
        return connections;
    }

    public void setMap(MapSPI map) {
        this.map = map;
    }
}
