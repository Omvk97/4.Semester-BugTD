/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.commontower.Tower;

/**
 *
 * @author oliver
 */
public class MapChangedDuringRoundEvent extends Event {
    
    public MapChangedDuringRoundEvent(Tower source) {
        super(source);
    }
    
    public Tower getTower() {
        return (Tower) source;
    }
    
}
