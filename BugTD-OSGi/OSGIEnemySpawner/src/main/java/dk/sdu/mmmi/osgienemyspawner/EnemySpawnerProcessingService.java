package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonai.events.EnemySpawnedEvent;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.commonmap.MapSPI;
import java.util.List;

public class EnemySpawnerProcessingService implements IEntityProcessingService {

    private MapSPI mapSPI;

    @Override
    public void process(GameData gameData, World world) {
        if (mapSPI == null) {
            return;
        }

        // Find and set EnemySpawner entitiy in world
        List<Entity> entities = world.getEntities(EnemySpawner.class);
        if (entities.size() < 1) {
            return;
        }
        EnemySpawner enemySpawner = (EnemySpawner) entities.get(0);

        if (enemySpawner.getWaves().isEmpty()) {
            enemySpawner.setWaves(mapSPI.getMapWaves());
        }

        enemySpawner.updateWaveInfo();
        enemySpawner.getEnemyAmountInWorld(world, gameData);
        enemySpawner.calculateCurrentRound(gameData);
        enemySpawner.incrementLastSpawn(gameData.getDelta());
        if (!Enemy.enemyRemovedFromGame) {
            if (enemySpawner.getEnemiesLeftToSpawn() > 0) {
                if (enemySpawner.getLastSpawn() > enemySpawner.getSpawnTime()) {
                    // System.out.println("Spawning enemy");
                    if ("Ground".equals(enemySpawner.getEnemyType())) {
                        Entity enemy = Enemy.createGroundEnemy(mapSPI.getEnemySpawnX(), mapSPI.getEnemySpawnY(), enemySpawner.getCurrentWave().getEnemyLife());
                        world.addEntity(enemy);
                        gameData.addEvent(new EnemySpawnedEvent(enemy));
                    } else if ("Flying".equals(enemySpawner.getEnemyType())) {
                        Entity enemy = Enemy.createFlyingEnemy(mapSPI.getEnemySpawnX(), mapSPI.getEnemySpawnY(), enemySpawner.getCurrentWave().getEnemyLife());
                        world.addEntity(enemy);
                        gameData.addEvent(new EnemySpawnedEvent(enemy));
                    }

                    enemySpawner.resetLastSpawn();
                }
            }
        }
    }

    public MapSPI getMapSPI() {
        return mapSPI;
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
    }

    public void removeMapSPI(MapSPI mapSPI) {
        this.mapSPI = null;
    }
}
