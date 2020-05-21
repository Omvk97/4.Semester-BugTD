/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar.scorers;

import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.Tile;
import dk.sdu.mmmi.commonplayer.Player;
import dk.sdu.mmmi.commontower.TowerPreview;

/**
 *
 * @author oliver
 */
public class TilePathCostScorer implements Scorer {

    private final Class[] entitiesToIgnore = {Enemy.class, TowerPreview.class, Player.class};

    @Override
    public double computeCost(Tile from, Tile to, MapSPI map) {
        if (!to.isWalkable() || map.checkIfTileIsOccupied(to, entitiesToIgnore)) {
            return Double.POSITIVE_INFINITY;
        } else {
            return 16;
        }
    }
    
}
