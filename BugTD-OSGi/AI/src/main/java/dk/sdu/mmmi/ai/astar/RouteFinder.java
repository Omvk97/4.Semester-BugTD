/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.ai.astar.scorers.Scorer;
import dk.sdu.mmmi.commonmap.Direction;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author oliver
 */
public class RouteFinder {

    private final Scorer pathCostScorer;
    private final Scorer heuristicScorer;

    public RouteFinder(Scorer pathCostScorer, Scorer heuristicScorer) {
        this.pathCostScorer = pathCostScorer;
        this.heuristicScorer = heuristicScorer;
    }

    public List<Tile> findRoute(Tile from, Tile goal, MapSPI map) {
        Queue<RouteNode> fringe = new PriorityQueue<>();
        Map<Tile, RouteNode> allNodes = new HashMap<>();

        RouteNode start = new RouteNode(from, null, 0d, heuristicScorer.computeCost(from, goal, map));
        fringe.add(start);
        allNodes.put(from, start);

        while (!fringe.isEmpty()) {

            RouteNode next = fringe.poll();

            if (next.getCurrent().equals(goal)) {
                List<Tile> route = new ArrayList<>();
                RouteNode current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while (current != null);

                return route;
            }

            for (Direction direction : Direction.values()) {
                try {
                    Tile neighbor = map.getTileInDirection(next.getCurrent(), direction);   // Throws ArrayIndexOutOfBoundsException if direction points out of map
                    RouteNode neighborRouteNode = allNodes.getOrDefault(neighbor, new RouteNode(neighbor));

                    double newPathScore = next.getRouteScore() + pathCostScorer.computeCost(next.getCurrent(), neighbor, map);
                    if (neighbor.equals(goal)) {
                        newPathScore = 0;
                    }

                    if (newPathScore < neighborRouteNode.getRouteScore()) {
                        neighborRouteNode.setPrevious(next.getCurrent());
                        neighborRouteNode.setRouteScore(newPathScore);
                        neighborRouteNode.setEstimatedScore(newPathScore + heuristicScorer.computeCost(neighbor, goal, map));
                        fringe.add(neighborRouteNode);
                        allNodes.put(neighbor, neighborRouteNode);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Do nothing
                }
            }
        }

        throw new IllegalStateException("No route found");
    }
}
