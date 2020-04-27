package dk.sdu.mmmi.osgienemyspawner;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.EnemyDiedEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.GameOverEvent;
import dk.sdu.mmmi.cbse.common.events.GameWonEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.MapWave;

import java.util.ArrayList;

public class EnemySpawner implements IEntityProcessingService {

    private int totalEnemysInWorld = 0;
    private int targetEnemyCount = 0;
    private int deadEnemies = 0;
    private int enemiesLeftToSpawn = 0;
    private float nextRoundDelay = 10f;
    private float nextRoundTimer = 0f;
    private float lastSpawn = 0f;
    private float spawnTime = 1f;
    private int currentWaveNumber = 1;
    private String enemyType;
    private MapWave currentWave = null;
    private MapSPI mapSPI;
    private ArrayList<MapWave> waves = new ArrayList<>();

    @Override
    public void process(GameData gameData, World world) {
        if (waves.isEmpty()) {
            waves = mapSPI.getMapWaves();
        }
        updateWaveInfo();
        getEnemyAmountInWorld(world, gameData);
        calculateCurrentRound(gameData);
        lastSpawn = lastSpawn + gameData.getDelta();

        if (enemiesLeftToSpawn > 0) {
            if (lastSpawn > spawnTime) {
                System.out.println("Spawning enemy");
                if ("Ground".equals(enemyType)) {
                    Entity enemy = Enemy.createGroundEnemy(mapSPI.getEnemySpawnX(), mapSPI.getEnemySpawnY(), currentWave.getEnemyLife());
                    world.addEntity(enemy);
                    gameData.addEvent(new EnemySpawnedEvent(enemy));
                } else if ("Flying".equals(enemyType)) {
                    Entity enemy = Enemy.createFlyingEnemy(mapSPI.getEnemySpawnX(), mapSPI.getEnemySpawnY(), currentWave.getEnemyLife());
                    world.addEntity(enemy);
                    gameData.addEvent(new EnemySpawnedEvent(enemy));
                }
                
                lastSpawn = 0f;
            }
        }
    }

    //Counts the amount of enemies in the world, and updates the total.
    private void getEnemyAmountInWorld(World world, GameData gameData) {
        int enemiesInWorld = 0;
        if (!gameData.getEvents(EnemyDiedEvent.class).isEmpty()) {
            for (Event enemyDiedEvent : gameData.getEvents(EnemyDiedEvent.class)) {
                deadEnemies++;
                gameData.removeEvent(enemyDiedEvent);
            }
        }
        for (Entity enemy : world.getEntities(Enemy.class)) {
            enemiesInWorld++;
        }

        totalEnemysInWorld = enemiesInWorld;
        int rest = enemiesInWorld + deadEnemies;
        enemiesLeftToSpawn = targetEnemyCount - (enemiesInWorld + deadEnemies);
    }

    // Gets all info from the MapWave and updates the variables.
    private void updateWaveInfo() {
        currentWave = waves.get(currentWaveNumber - 1);
        targetEnemyCount = currentWave.getEnemyAmount();
        enemyType = currentWave.getEnemyType();

    }

    public MapSPI getMapSPI() {
        return mapSPI;
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
    }

    public void calculateCurrentRound(GameData gd) {
        if (deadEnemies == targetEnemyCount) {
            nextRoundTimer = nextRoundTimer + gd.getDelta();
            if (nextRoundTimer > nextRoundDelay) {
                if (currentWaveNumber < waves.size()) {
                    if (waves.get(currentWaveNumber) != null) {
                        currentWaveNumber++;
                        updateWaveInfo();
                        deadEnemies = 0;
                        enemiesLeftToSpawn = 0;
                        totalEnemysInWorld = 0;
                    }
                } else {
                    gd.setMenuFlashMessage("Congratulations, you beat the level!");
                    gd.addEvent(new GameWonEvent(null));
                }
                nextRoundTimer = 0f;
            }
        }
    }
}
