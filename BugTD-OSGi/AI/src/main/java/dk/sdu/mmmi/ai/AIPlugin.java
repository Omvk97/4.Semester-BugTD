package dk.sdu.mmmi.ai;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class AIPlugin implements IGamePluginService {

    private static boolean newGame;
    
    @Override
    public void start(GameData gameData, World world) {
        newGame = true;
    }

    @Override
    public void stop(GameData gameData, World world) {
    }

    public static boolean isNewGame() {
        return newGame;
    }

    public static void setNewGame(boolean newGame) {
        AIPlugin.newGame = newGame;
    }

}
