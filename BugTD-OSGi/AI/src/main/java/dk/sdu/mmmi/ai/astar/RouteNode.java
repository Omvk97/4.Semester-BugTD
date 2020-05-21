/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import dk.sdu.mmmi.commonmap.Tile;
import java.util.StringJoiner;

/**
 *
 * @author oliver
 */
public class RouteNode implements Comparable<RouteNode> {

    private final Tile current;
    private Tile previous;
    private double routeScore;
    private double estimatedScore;

    RouteNode(Tile current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    RouteNode(Tile current, Tile previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode other) {
        if (this.estimatedScore > other.estimatedScore) {
            return 1;
        } else if (this.estimatedScore < other.estimatedScore) {
            return -1;
        } else {
            return 0;
        }
    }

    public Tile getCurrent() {
        return current;
    }
    
    public Tile getPrevious() {
        return previous;
    }

    public void setPrevious(Tile previous) {
        this.previous = previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", RouteNode.class.getSimpleName() + "[", "]").add("current=" + current)
            .add("previous=" + previous).add("routeScore=" + routeScore).add("estimatedScore=" + estimatedScore)
            .toString();
    }
    
}
