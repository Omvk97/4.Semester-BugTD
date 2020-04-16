package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;
import dk.sdu.mmmi.enemy.GroundEnemy;
import java.util.Timer;
import javafx.concurrent.Task;

public class EnemySpawner implements IEntityProcessingService {

    private int totalEnemysInWorld = 0;
    private int targetEnemyCount = 5;
    private float timeDelay = 30f;
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
