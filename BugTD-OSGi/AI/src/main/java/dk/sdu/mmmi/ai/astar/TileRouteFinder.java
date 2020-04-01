/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.commonmap.Tile;
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
    
    public void test(Tile[][] mapTiles) throws Exception {
        Set<MapTile> tiles = new HashSet<MapTile>();
        Map<String, Set<String>> connections = new HashMap<>();
        
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
}
