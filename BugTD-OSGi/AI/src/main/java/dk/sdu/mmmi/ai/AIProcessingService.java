/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.ai.astar.TileRouteFinder;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonai.events.TowerPlacedDuringRoundEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oliver
 */
public class AIProcessingService implements IEntityProcessingService {
    
    private MapSPI mapSPI;
    private int counter = 0;

    @Override
    public void process(GameData gameData, World world) {
        if (counter == 0) {
            TileRouteFinder test = new TileRouteFinder(mapSPI);
            
            try {
                test.test(mapSPI.getTiles(), mapSPI.getTiles()[0][0], mapSPI.getTiles()[5][5]);
            } catch (Exception ex) {
                Logger.getLogger(AIProcessingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }
        
        Set<Enemy> enemiesToCalculate = new HashSet<Enemy>(); // Set to avoid duplicates if enemy is both spwaning and tower is placed at the same time
        // Enemy Spawned event listener
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
        for (Enemy enemy : enemiesToCalculate) {
            // TODO
        }
        
        // Create event with enemy source and the path they should take
    }

    public MapSPI getMapSPI() {
        return mapSPI;
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
    }
}
