package dk.sdu.mmmi.cbse;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import dk.sdu.mmmi.enemy.EnemyControlSystem;
import dk.sdu.mmmi.enemy.GroundEnemy;
import java.util.ArrayList;
import java.util.Comparator;
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
    private static final AssetManager assetManager = new AssetManager();
    private Animation animation;
    private TextureAtlas textureAtlas;
    private float elapsedTime = 0f;
    SpriteBatch batch;
    SpriteBatch batch2;

    public Game() {
        init();
    }

    public void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "BugTD";
        cfg.width = 832;
        cfg.height = 832;
        cfg.useGL30 = false;
        cfg.resizable = false;
        gameData.setDisplayWidth(cfg.width);
        gameData.setDisplayHeight(cfg.height);
        
        new LwjglApplication(this, cfg);
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
        batch = new SpriteBatch();
        batch2 = new SpriteBatch();
        loadAssets();
        loadAnimations();

    }

    public void loadAnimations() {
        for (Entity entity : world.getEntities()) {
            AnimationPart animationPart = entity.getPart(AnimationPart.class);
            if (animationPart != null) {
                textureAtlas = new TextureAtlas(Gdx.files.internal(animationPart.getAtlasPath()));
                animation = new Animation(1f / 15f, textureAtlas.getRegions());
            }
        }
        System.out.println(textureAtlas.getRegions().toString());
        
    }

    public void loadAssets() {
        
        for (Entity entity : world.getEntities()) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            if (spritePart != null) {
                Game.assetManager.load(spritePart.getSpritePath(), Texture.class);
                Game.assetManager.update();
            }
        }
         Game.assetManager.finishLoading();

    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
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
        loadAnimations();
        ArrayList<Entity> entitiesToAnimate = new ArrayList<>();

        // Populate list
        for (Entity entity : world.getEntities()) {
            AnimationPart animationPart = entity.getPart(AnimationPart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);
            System.out.println(textureAtlas.getRegions());
            
            if (animationPart != null && positionPart != null) {
                entitiesToAnimate.add(entity);
            }
        }

        // Draw
        for (Entity entity : entitiesToAnimate) {
            AnimationPart animationPart = entity.getPart(AnimationPart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);
            
            drawAnimation(animationPart, positionPart);
        }

        
        

        ArrayList<Entity> entitiesToDraw = new ArrayList<>();

        // Populate list
        for (Entity entity : world.getEntities()) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);

            if (spritePart != null && positionPart != null) {
                entitiesToDraw.add(entity);
            }
        }

        // Sort by layer
        entitiesToDraw.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                SpritePart spritePart1 = e1.getPart(SpritePart.class);
                SpritePart spritePart2 = e2.getPart(SpritePart.class);
                return spritePart1.getLayer() - spritePart2.getLayer();
            }
        });

        // Draw
        for (Entity entity : entitiesToDraw) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);
            
            drawSprite(spritePart, positionPart);
            
        }
    }

    private void drawAnimation(AnimationPart anima, PositionPart pos) {
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsedTime, true), pos.getX(), pos.getY());
        batch.end();
    }

    private void drawSprite(SpritePart spritePart, PositionPart positionPart) {
        batch2.begin();
        Texture texture = assetManager.get(spritePart.getSpritePath(), Texture.class);
        Sprite sprite = new Sprite(texture);
        sprite.rotate((float) Math.toDegrees(positionPart.getRadians()));
        sprite.setX(positionPart.getX());
        sprite.setY(positionPart.getY());
        sprite.setSize(spritePart.getWidth(), spritePart.getHeight());
        sprite.draw(batch2);
        batch2.end();
        
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
        batch.dispose();
        textureAtlas.dispose();
        batch2.dispose();
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
