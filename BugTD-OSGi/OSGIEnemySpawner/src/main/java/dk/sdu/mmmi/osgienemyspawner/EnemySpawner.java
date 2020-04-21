package dk.sdu.mmmi.osgienemyspawner;


import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.MapWave;
import dk.sdu.mmmi.enemy.FlyingEnemy;
import dk.sdu.mmmi.enemy.GroundEnemy;
import java.util.ArrayList;

public class EnemySpawner implements IEntityProcessingService {

    private int totalEnemysInWorld = 0;
    private int targetEnemyCount = 0;
    private float timeDelay = 30f;
    private float lastSpawn = 0f;
    private int currentWaveNumber = 1;
    private String enemyType;
    private MapWave currentWave = null; 
    private MapSPI mapSPI;
    private ArrayList<MapWave> waves = new ArrayList<>(); 
    
    
    
    @Override
    public void process(GameData gameData, World world) {
        updateWaveInfo();
        getEnemyAmountInWorld(world);
     
         
        if (totalEnemysInWorld < targetEnemyCount) {
            if (lastSpawn > timeDelay) {
                if ("Ground".equals(enemyType)) {
                GroundEnemy.createGroundEnemy(gameData, world);
                }
                else if ("Flying".equals(enemyType)) {
                FlyingEnemy.createFlyingEnemy(gameData, world);
                }
                
                
                lastSpawn = 0f;
            }
            lastSpawn = lastSpawn + 0.5f;
        }
    }
    
    private void getEnemyAmountInWorld(World world) {
        int enemiesInWorld = 0;
        for(Entity enemy : world.getEntities(Enemy.class)){
            enemiesInWorld++;
        }
        totalEnemysInWorld = enemiesInWorld;
    }
    
    private void updateWaveInfo() {
        currentWave = waves.get(currentWaveNumber - 1);
        targetEnemyCount = currentWave.getEnemyAmount();
        enemyType = currentWave.getEnemyType();
       
        //This might need to be changed with a "newWaveEvent" or so.
        //currentWaveNumber = ...
    }
    
    public MapSPI getMapSPI() {
        return mapSPI;
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
        waves = mapSPI.getMapWaves();
    }
}
