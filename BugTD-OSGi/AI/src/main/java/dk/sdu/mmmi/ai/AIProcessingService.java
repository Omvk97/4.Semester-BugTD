/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonai.events.TowerPlacedDuringRoundEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author oliver
 */
public class AIProcessingService implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        // Enemy Spawned event listener
        Set<Enemy> enemiesToCalculate = new HashSet<Enemy>();
        for (Event event : gameData.getEvents(EnemySpawnedEvent.class)) {
            EnemySpawnedEvent enemySpawnedEvent = (EnemySpawnedEvent) event;
            enemiesToCalculate.add(enemySpawnedEvent.getEnemy());
        }
        
        // Tower set event listener which should trigger re calibration of all enemies
        if ((gameData.getEvents(TowerPlacedDuringRoundEvent.class)).size() != 0) {
            for (Entity enemy : world.getEntities(Enemy.class)) {
                enemiesToCalculate.add((Enemy) enemy);
            }
        }
        
        // Calculate best route for enemy
        
        // Create event with enemy source and the path they should take
    }
    
}
