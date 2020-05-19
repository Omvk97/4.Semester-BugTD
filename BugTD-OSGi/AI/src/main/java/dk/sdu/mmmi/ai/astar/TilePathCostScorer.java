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
public class TilePathCostScorer implements Scorer<MapTile>{

    @Override
    public double computeCost(MapTile from, MapTile goal) {
        return 16;
    }
    
}
