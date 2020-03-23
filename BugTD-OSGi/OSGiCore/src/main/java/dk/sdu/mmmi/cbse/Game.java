package dk.sdu.mmmi.cbse;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.EntityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import java.io.InputStream;
import java.net.URL;
import dk.sdu.mmmi.enemy.EnemyControlSystem;
import dk.sdu.mmmi.enemy.GroundEnemy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private final GameData gameData = new GameData();
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();
    private AssetManager assetManager;
    
    public Game() {
        init();
    }

    public void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Asteroids";
        cfg.width = 800;
        cfg.height = 600;
        cfg.useGL30 = false;
        cfg.resizable = false;
        gameData.setDisplayWidth(cfg.width);
        gameData.setDisplayHeight(cfg.height);

        new LwjglApplication(this, cfg);
        assetManager = new AssetManager();
    }

    @Override
    public void create() {
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        IGamePluginService groundEnemyPlugin = new GroundEnemy();
        IEntityProcessingService enemyProcess = new EnemyControlSystem();
        
        entityProcessorList.add(enemyProcess);
        gamePluginList.add(groundEnemyPlugin);
        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        
        loadAssets();
    }
    
    public void loadAssets() {
        for (Entity entity : world.getEntities()) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            if (spritePart != null) {
                assetManager.load(spritePart.getSpritePath(), Texture.class);
            }
        }
        assetManager.finishLoading();
    }
    

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();

        update();
        draw();
    }

    private void update() {
        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessorList) {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessorList) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        for (Entity entity : world.getEntities()) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);
            if (spritePart != null && positionPart != null) {
                drawSprite(spritePart, positionPart, spriteBatch);
            }
        }
        spriteBatch.end();
    }
    
    private void drawSprite(SpritePart spritePart, PositionPart positionPart, SpriteBatch spriteBatch){
        Texture texture = assetManager.get(spritePart.getSpritePath(), Texture.class);
        Sprite sprite = new Sprite(texture);
        sprite.rotate((float) Math.toDegrees(positionPart.getRadians()));
        sprite.setX(positionPart.getX());
        sprite.setY(positionPart.getY());
        sprite.setSize(spritePart.getWidth(), spritePart.getHeight());
        sprite.draw(spriteBatch);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
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
