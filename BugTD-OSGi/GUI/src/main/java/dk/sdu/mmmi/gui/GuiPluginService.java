package dk.sdu.mmmi.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.gui.input.GameInputProcessor;
import dk.sdu.mmmi.gui.screens.GameScreen;
import dk.sdu.mmmi.gui.screens.MenuScreen;

public class GuiPluginService extends com.badlogic.gdx.Game implements IGamePluginService {

    private static OrthographicCamera cam;
    GameData gameData;
    World world;
    public static GuiPluginService instance = null;

    public void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "BugTD";
        cfg.width = 832;
        cfg.height = 832;
        cfg.useGL30 = false;
        cfg.resizable = false;
        gameData.setDisplayWidth(cfg.width);
        gameData.setDisplayHeight(cfg.height);
        instance = this;

        new LwjglApplication(this, cfg);
    }

    public static GuiPluginService getInstance() {
        return instance;
    }

    public void startGame() {
        this.setScreen(new GameScreen(gameData, world));
    }

    @Override
    public void create() {
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        this.setScreen(new MenuScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void start(GameData gameData, World world) {
        this.gameData = gameData;
        this.world = world;
        init();
    }

    @Override
    public void stop(GameData gameData, World world) {
    }
}
