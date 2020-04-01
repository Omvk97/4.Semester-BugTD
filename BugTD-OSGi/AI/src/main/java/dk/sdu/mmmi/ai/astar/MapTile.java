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
public class MapTile implements GraphNode{
    private String id;
    private float x;
    private float y;

    public MapTile(String id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public String getPosition() {
        return "(" + x + ";" + y + ")";
    }
}
