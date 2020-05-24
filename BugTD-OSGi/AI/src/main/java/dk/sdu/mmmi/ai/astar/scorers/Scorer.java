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
public interface Scorer {
    double computeCost(Tile from, Tile goal, MapSPI map);
}
