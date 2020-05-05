package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.ai.astar.TileRouteFinder;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;

public class AIPlugin implements IGamePluginService {

    private static boolean newGame;
    private static MapSPI mapSPI;
    private static TileRouteFinder routeFinder;
    
    @Override
    public void start(GameData gameData, World world) {
        newGame = true;
    }

    @Override
    public void stop(GameData gameData, World world) {
        TileRouteFinder.resetInstance();
        routeFinder = null;
        AIPlugin.setNewGame(true);
    }

    public static boolean isNewGame() {
        return newGame;
    }

    public static void setNewGame(boolean newGame) {
        AIPlugin.newGame = newGame;
    }

    public static MapSPI getMapSPI() {
        return mapSPI;
    }

    public static void setMapSPI(MapSPI mapSPI) {
        AIPlugin.mapSPI = mapSPI;
    }

    public static TileRouteFinder getRouteFinder() {
        return routeFinder;
    }

    public static void setRouteFinder(TileRouteFinder routeFinder) {
        AIPlugin.routeFinder = routeFinder;
    }
}
