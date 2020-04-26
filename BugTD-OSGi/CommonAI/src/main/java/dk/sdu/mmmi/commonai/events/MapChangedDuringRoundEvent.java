/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;

/**
 *
 * @author oliver
 */
public class MapChangedDuringRoundEvent extends Event {

    public MapChangedDuringRoundEvent(Entity source) {
        super(source);
    }

    public Entity getTower() {
        return source;
    }

}
