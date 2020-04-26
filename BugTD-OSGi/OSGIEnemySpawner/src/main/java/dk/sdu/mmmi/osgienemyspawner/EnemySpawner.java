package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.MapWave;

import java.util.ArrayList;

public class EnemySpawner implements IEntityProcessingService {

    private int totalEnemysInWorld = 0;
    private int targetEnemyCount = 0;
    private float lastSpawn = 0f;
    private float spawnTime = 2f;
    private int currentWaveNumber = 1;
    private String enemyType;
    private MapWave currentWave = null;
    private MapSPI mapSPI;
    private ArrayList<MapWave> waves = new ArrayList<>();

    @Override
    public void process(GameData gameData, World world) {
        if (waves.isEmpty()){
            waves = mapSPI.getMapWaves();
        }
        updateWaveInfo();
        getEnemyAmountInWorld(world);
        lastSpawn = lastSpawn + gameData.getDelta();

        if (totalEnemysInWorld < targetEnemyCount) {
            if (lastSpawn > spawnTime) {

                if ("Ground".equals(enemyType)) {
                    world.addEntity(Enemy.createGroundEnemy(400, 400));
                } else if ("Flying".equals(enemyType)) {
                    world.addEntity(Enemy.createFlyingEnemy(400, 400));
                }

                lastSpawn = 0f;
            }
        }
    }

    //Counts the amount of enemies in the world, and updates the total.
    private void getEnemyAmountInWorld(World world) {
        int enemiesInWorld = 0;
        for (Entity enemy : world.getEntities(Enemy.class)) {
            enemiesInWorld++;
        }
        totalEnemysInWorld = enemiesInWorld;
    }

    // Gets all info from the MapWave and updates the variables.
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
        //waves = mapSPI.getMapWaves();
    }
}
