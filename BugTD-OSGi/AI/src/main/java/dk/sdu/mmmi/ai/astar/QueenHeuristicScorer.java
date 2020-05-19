/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

/**
 *
 * @author oliver
 */
public class QueenHeuristicScorer implements Scorer<MapTile>{

    @Override
    public double computeCost(MapTile from, MapTile queenTile) {
        double ac = Math.abs(queenTile.getY() - from.getY());
        double cb = Math.abs(queenTile.getX() - from.getX());
        
        return Math.hypot(ac, cb);
    }
    
}
