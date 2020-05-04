package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.EnemyDiedEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.GameWonEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commonmap.MapWave;

import java.util.ArrayList;

public class EnemySpawner extends Entity {
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
    private ArrayList<MapWave> waves = new ArrayList<>();

    // Gets all info from the MapWave and updates the variables.
    public void updateWaveInfo() {
        currentWave = waves.get(currentWaveNumber - 1);
        targetEnemyCount = currentWave.getEnemyAmount();
        enemyType = currentWave.getEnemyType();
    }

    //Counts the amount of enemies in the world, and updates the total.
    public void getEnemyAmountInWorld(World world, GameData gameData) {
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

    public int getEnemiesLeftToSpawn() {
        return enemiesLeftToSpawn;
    }

    public float getLastSpawn() {
        return lastSpawn;
    }

    public void incrementLastSpawn(float time) {
        this.lastSpawn += time;
    }

    public float getSpawnTime() {
        return spawnTime;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public MapWave getCurrentWave() {
        return currentWave;
    }

    public ArrayList<MapWave> getWaves() {
        return waves;
    }

    public void setWaves(ArrayList<MapWave> waves) {
        this.waves = waves;
    }

    public void resetLastSpawn() {
        this.lastSpawn = 0f;
    }
}
