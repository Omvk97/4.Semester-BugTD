/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

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
public class RouteFinder<T extends GraphNode> {

    private final Graph<T> graph;
    private final Scorer<T> pathCostScorer;
    private final Scorer<T> heuristicScorer;

    public RouteFinder(Graph<T> graph, Scorer<T> pathCostScorer, Scorer<T> heuristicScorer) {
        this.graph = graph;
        this.pathCostScorer = pathCostScorer;
        this.heuristicScorer = heuristicScorer;
    }

    public List<T> findRoute(T from, T goal) {
        Queue<RouteNode> fringe = new PriorityQueue<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        RouteNode<T> start = new RouteNode<>(from, null, 0d, heuristicScorer.computeCost(from, goal));
        fringe.add(start);
        allNodes.put(from, start);

        while (!fringe.isEmpty()) {

            RouteNode<T> node = fringe.poll();

            if (node.getCurrent().equals(goal)) {
                List<T> route = new ArrayList<>();
                RouteNode<T> current = node;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while (current != null);

                // System.out.println("Final Route: " + route);
                return route;
            }

            graph.getConnections(node.getCurrent()).forEach(connection -> {
                RouteNode<T> neighbor = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                allNodes.put(connection, neighbor);

                double newPathCost = node.getPathCost() + pathCostScorer.computeCost(node.getCurrent(), connection);
                if (newPathCost < neighbor.getPathCost()) {
                    neighbor.setPrevious(node.getCurrent());
                    neighbor.setPathCost(newPathCost);
                    neighbor.setEstimatedScore(newPathCost + heuristicScorer.computeCost(connection, goal));
                    fringe.add(neighbor);
                }
            });
        }

        throw new IllegalStateException("No route found");
    }
}
