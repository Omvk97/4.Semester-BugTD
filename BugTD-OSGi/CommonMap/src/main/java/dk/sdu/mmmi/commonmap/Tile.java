/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonmap;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author oliver
 */
public class Tile extends Entity {
    
    private boolean walkable;

    public Tile(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
    
}
