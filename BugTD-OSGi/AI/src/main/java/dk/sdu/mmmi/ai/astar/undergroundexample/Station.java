/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar.undergroundexample;

import dk.sdu.mmmi.ai.astar.GraphNode;
import java.util.StringJoiner;

/**
 *
 * @author oliver
 */
public class Station implements GraphNode {
    private String id;
    private String name;
    private double latitude;
    private double longitude;

    public Station(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Station.class.getSimpleName() + "[", "]").add("id='" + id + "'")
            .add("name='" + name + "'").add("latitude=" + latitude).add("longitude=" + longitude).toString();
    }

    
    
}
