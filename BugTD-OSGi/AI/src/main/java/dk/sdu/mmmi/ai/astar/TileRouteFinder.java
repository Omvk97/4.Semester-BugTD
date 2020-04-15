/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commontower.TowerPreview;
import java.util.ArrayList;
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
    private Class[] entitiesToIgnore = {Enemy.class, TowerPreview.class };


    public TileRouteFinder(MapSPI map) {
        this.map = map;
    }
    
    private Graph<MapTile> mapTilesGraph;

    private RouteFinder<MapTile> routeFinder;
    
    public List<Tile> findBestRoute(Tile[][] tiles, T startTile, T goalTile) throws Exception {
        Set<MapTile> mapTiles = new HashSet<MapTile>();
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                mapTiles.add(new MapTile(tile.getID(), tile));
            }
        }
        
        Map<String, Set<String>> connections = getConnections(tiles);

        
        mapTilesGraph = new Graph<>(mapTiles, connections);
        routeFinder = new RouteFinder<>(mapTilesGraph, new TilePathCostScorer(), new QueenHeuristicScorer());
        
        List<MapTile> route = routeFinder.findRoute(mapTilesGraph.getNode(startTile.getID()), mapTilesGraph.getNode(goalTile.getID()));
        
        return route.stream().map(MapTile::getTile).collect(Collectors.toList());
    }

    // TODO - only calculate this if the map has changed since last calculation
    private Map<String, Set<String>> getConnections(Tile[][] tiles) {
        
        HashMap<String, Set<String>> connections = new HashMap<>();
        
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                HashSet<String> neighbors = new HashSet<String>();
                for (Direction direction : Direction.values()) {
                    try {
                        Tile neighbor = map.getTileInDirection(tile, direction);
                        // Add neighbor if it is not occupied by anything else than an enemy or towerPreview
                        if (!map.checkIfTileIsOccupied(neighbor, entitiesToIgnore)) {
                            neighbors.add(neighbor.getID());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Don't add
                    }
                }
                for (String neighbor : neighbors) {
                    System.out.println("neightbor! " +  neighbor);
                }
                connections.put(tile.getID(), neighbors);
            }
        }
        return connections;
    }
}
