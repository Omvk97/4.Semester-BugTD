/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonmap;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;

/**
 *
 * @author oliver
 */
public class Tile extends Entity {
    
    private boolean walkable;
    private PositionPart positionPart;
    private SpritePart spritePart;
    
    public Tile(boolean walkable, SpritePart spritePart, PositionPart positionPart) {
        this.walkable = walkable;
        add(spritePart);
        add(positionPart);
        this.spritePart = spritePart;
        this.positionPart = positionPart;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public float getX() {
        return this.positionPart.getX();
    }

    public float getY() {
        return this.positionPart.getY();
    }
    
}
