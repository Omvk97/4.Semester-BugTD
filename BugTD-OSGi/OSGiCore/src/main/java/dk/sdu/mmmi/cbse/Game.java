package dk.sdu.mmmi.cbse;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {


    private final GameData gameData = new GameData();
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();
    private static Game instance = null;

    public Game() {
        instance = this;
    }

    public static Game getInstance() {
        return instance;
    }

    public World getWorld() {
        return world;
    }

    public GameData getGameData() {
        return gameData;
    }
    
    public void restart(List<IGamePluginService> ignoreThese) {
        // Stop all
        for (IGamePluginService plugin : gamePluginList) {
            if (ignoreThese.contains(plugin)) {
                continue;
            }
            plugin.stop(gameData, world);
        }
        
        // Start all
        for (IGamePluginService plugin : gamePluginList) {
            if (ignoreThese.contains(plugin)) {
                continue;
            }
            plugin.start(gameData, world);
        }
    }

    public void update() {

        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessorList) {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessorList) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    public void addEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.add(eps);
    }

    public void removeEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.remove(eps);
    }

    public void addPostEntityProcessingService(IPostEntityProcessingService eps) {
        postEntityProcessorList.add(eps);
    }

    public void removePostEntityProcessingService(IPostEntityProcessingService eps) {
        postEntityProcessorList.remove(eps);
    }

    public void addGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.add(plugin);
        plugin.start(gameData, world);
    }

    public void removeGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.remove(plugin);
        plugin.stop(gameData, world);
    }

}
