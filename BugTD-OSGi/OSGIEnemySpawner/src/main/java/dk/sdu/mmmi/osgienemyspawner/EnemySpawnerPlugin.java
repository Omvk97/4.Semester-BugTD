package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemySpawnerPlugin implements IGamePluginService {

    EnemySpawner enemySpawner;

    @Override
    public void start(GameData gameData, World world) {
        this.enemySpawner = createEnemySpawner();
        world.addEntity(this.enemySpawner);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemySpawner);
    }

    private EnemySpawner createEnemySpawner() {
        EnemySpawner es = new EnemySpawner();
        return es;
    }
}
