package dk.sdu.mmmi.osgienemyspawner;


import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.enemy.GroundEnemy;

public class EnemySpawner implements IEntityProcessingService {

    private int totalEnemysInWorld = 0;
    private int targetEnemyCount = 5;
    private float timeDelay = 60f;
    private float lastSpawn = 0f;

    @Override
    public void process(GameData gameData, World world) {
        if (totalEnemysInWorld < targetEnemyCount) {
            if (lastSpawn > timeDelay) {
                GroundEnemy.createGroundEnemy(gameData, world);
                
                lastSpawn = 0f;
                totalEnemysInWorld++;

            }
            lastSpawn = lastSpawn + 0.5f;
        }
    }
}
