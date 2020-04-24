/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonenemy.EnemyCommand;
import java.util.List;

/**
 *
 * @author oliver
 */
public class RouteCalculatedEvent extends Event {
    
    private List<EnemyCommand> enemyCommands;
    
    public RouteCalculatedEvent(Enemy source, List<EnemyCommand> commands) {
        super(source);
        this.enemyCommands = commands;
    }
    
    public Enemy getEnemy() {
        return (Enemy) source;
    }

    public List<EnemyCommand> getEnemyCommands() {
        return enemyCommands;
    }
}
