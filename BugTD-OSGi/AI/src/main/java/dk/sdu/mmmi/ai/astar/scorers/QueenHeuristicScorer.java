/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar.scorers;

import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;

/**
 *
 * @author oliver
 */
public class QueenHeuristicScorer implements Scorer {

    @Override
    public double computeCost(Tile from, Tile queenTile, MapSPI map) {
        double ac = Math.abs(queenTile.getY() - from.getY());
        double cb = Math.abs(queenTile.getX() - from.getX());

        return 5 * Math.hypot(ac, cb);
    }

}
