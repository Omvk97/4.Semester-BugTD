/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonmap;

/**
 *
 * @author oliver
 */
public interface MapSPI {
    
    Tile[][] getTiles();
    
    void loadFile(String filepath);
    
    
}
