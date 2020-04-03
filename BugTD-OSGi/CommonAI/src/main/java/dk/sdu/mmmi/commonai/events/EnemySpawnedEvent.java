/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.commonenemy.Enemy;

/**
 *
 * @author oliver
 */
public class EnemySpawnedEvent extends Event {
    
    public EnemySpawnedEvent(Enemy source) {
        super(source);
    }
    
    public Enemy getEnemy() {
        return (Enemy) source;
    }
    
}
