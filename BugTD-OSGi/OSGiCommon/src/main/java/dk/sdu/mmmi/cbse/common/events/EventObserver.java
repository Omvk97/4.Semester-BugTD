/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.events;

/**
 *
 * @author oliver
 */
public abstract class EventObserver {
    
    private boolean removeEvent;
    
    public boolean getRemoveEvent() {
        return this.removeEvent;
    }  
    
    public void setRemoveEvent(boolean removeEvent) {
        this.removeEvent = removeEvent;
    }
    
    abstract public void methodToCall(Event event);
}
