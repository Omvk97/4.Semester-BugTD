/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commontower.TowerPreview;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author oliver
 * @param <T>
 */
public class TileRouteFinder<T extends Tile> {

    private final MapSPI map;
    private final Class[] entitiesToIgnore = {Enemy.class, TowerPreview.class};
    private Map<String, Set<String>> groundConnections = null;
    private Map<String, Set<String>> flyingConnections = null;
    private static TileRouteFinder instance = null;

    private TileRouteFinder(MapSPI map) {
        this.map = map;
    }

    public static TileRouteFinder getInstance(MapSPI mapSPI) {
        if (instance == null) {
            instance = new TileRouteFinder(mapSPI);
        }
        return instance;
    }

    private Graph<MapTile> mapTilesGraph;

    private RouteFinder<MapTile> routeFinder;

    public List<Tile> findBestRouteForGroundEnemy(Tile[][] tiles, T startTile, T goalTile, boolean connectionsHaveChanged) throws IllegalStateException {
        Set<MapTile> mapTiles = new HashSet<>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                mapTiles.add(new MapTile(tile.getID(), tile));
            }
        }

        if (connectionsHaveChanged || groundConnections == null) {
            calculateGroundConnections(tiles, goalTile);
        }

        mapTilesGraph = new Graph<>(mapTiles, groundConnections);
        routeFinder = new RouteFinder<>(mapTilesGraph, new TilePathCostScorer(), new QueenHeuristicScorer());

        List<MapTile> route = routeFinder.findRoute(mapTilesGraph.getNode(startTile.getID()), mapTilesGraph.getNode(goalTile.getID()));

        return route.stream().map(MapTile::getTile).collect(Collectors.toList());
    }

    public List<Tile> findBestRouteForFlyingEnemy(Tile[][] tiles, T startTile, T goalTile) throws IllegalStateException {
        Set<MapTile> mapTiles = new HashSet<>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                mapTiles.add(new MapTile(tile.getID(), tile));
            }
        }

        if (flyingConnections == null) {
            calculateFlyConnections(tiles);
        }

        mapTilesGraph = new Graph<>(mapTiles, flyingConnections);

        routeFinder = new RouteFinder<>(mapTilesGraph, new TilePathCostScorer(), new QueenHeuristicScorer());

        List<MapTile> route = routeFinder.findRoute(mapTilesGraph.getNode(startTile.getID()), mapTilesGraph.getNode(goalTile.getID()));

        return route.stream().map(MapTile::getTile).collect(Collectors.toList());
    }

    private void calculateGroundConnections(Tile[][] tiles, Tile goalTile) {
        // System.out.println("Calculating Tile connections for Ground AI!");
        HashMap<String, Set<String>> calculatedConnections = new HashMap<>();

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                HashSet<String> neighbors = new HashSet<>();
                for (Direction direction : Direction.values()) {
                    try {
                        Tile neighbor = map.getTileInDirection(tile, direction);
                        // Only add neighbor if it's walkable
                        if (neighbor.isWalkable()) {
                            // Add neighbor if it is not occupied by anything else than an enemy or towerPreview
                            if (!map.checkIfTileIsOccupied(neighbor, entitiesToIgnore) || neighbor.getID().equals(goalTile.getID())) {
                                neighbors.add(neighbor.getID());
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Don't add
                    }
                }
                calculatedConnections.put(tile.getID(), neighbors);
            }
        }
        this.groundConnections = calculatedConnections;
    }

    private void calculateFlyConnections(Tile[][] tiles) {
        HashMap<String, Set<String>> calculatedConnections = new HashMap<>();
        // System.out.println("Calculating Tile Connections for Flying AI");
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                HashSet<String> neighbors = new HashSet<>();
                for (Direction direction : Direction.values()) {
                    try {
                        Tile neighbor = map.getTileInDirection(tile, direction);
                        // Only add neighbor if it's walkable
                        if (neighbor.isWalkable()) {
                            neighbors.add(neighbor.getID());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Don't add
                    }
                }
                calculatedConnections.put(tile.getID(), neighbors);
            }
        }
        this.flyingConnections = calculatedConnections;
    }
}
