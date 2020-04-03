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
public class TileRouteFinder<T extends Tile> {
    
    private MapSPI map;

    public TileRouteFinder(MapSPI map) {
        this.map = map;
    }
    
    private Graph<MapTile> mapTilesGraph;

    private RouteFinder<MapTile> routeFinder;
    
    public void test(Tile[][] tiles, T startTile, T goalTile) throws Exception {
        Set<MapTile> mapTiles = new HashSet<MapTile>();
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                mapTiles.add(new MapTile(tile.getID(), tile.getX(), tile.getY()));
            }
        }
        
        Map<String, Set<String>> connections = getConnections(tiles);

        
        mapTilesGraph = new Graph<>(mapTiles, connections);
        routeFinder = new RouteFinder<>(mapTilesGraph, new TilePathCostScorer(), new QueenHeuristicScorer());
        
        // TODO - Insert enemy position and queen position from tiles
        List<MapTile> route = routeFinder.findRoute(mapTilesGraph.getNode(startTile.getID()), mapTilesGraph.getNode(goalTile.getID()));
        System.out.println(route.stream().map(MapTile::getPosition).collect(Collectors.toList()));
    }

    private Map<String, Set<String>> getConnections(Tile[][] tiles) {
        
        HashMap<String, Set<String>> connections = new HashMap<>();
        
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                HashSet neighbors = new HashSet<String>();
                for (Direction direction : Direction.values()) {
                    try {
                        Tile neighbor = map.getTileInDirection(tile, direction);
                        neighbors.add(neighbor.getID());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Don't add
                    }
                }
                connections.put(tile.getID(), neighbors);
            }
        }
        return connections;
    }
}
