package dk.sdu.mmmi.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;

import java.util.ArrayList;
import java.util.Comparator;

public class GameScreen implements Screen {

    Stage stage;
    GameData gameData;
    World world;
    private float elapsedTime = 0f;
    SpriteBatch batch;
    SpriteBatch batch2;
    private static final AssetManager assetManager = new AssetManager();
    private Animation animation;
    private TextureAtlas textureAtlas;

    public GameScreen(GameData gameData, World world) {
        stage = new Stage();
        this.gameData = gameData;
        this.world = world;
        stage = new Stage();
        batch = new SpriteBatch();
        batch2 = new SpriteBatch();
        loadAssets();
        loadAnimations();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();
        draw();
    }



    private void draw() {
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


        loadAnimations();
        ArrayList<Entity> entitiesToAnimate = new ArrayList<>();


        // Populate list
        for (Entity entity : world.getEntities()) {
            AnimationPart animationPart = entity.getPart(AnimationPart.class);
            PositionPart positionPart = entity.getPart(PositionPart.class);
            // System.out.println(textureAtlas.getRegions());

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
        sprite.setAlpha(spritePart.getAlpha());
        sprite.setSize(spritePart.getWidth(), spritePart.getHeight());
        sprite.draw(batch2);
        batch2.end();

    }

    public void loadAnimations() {
        for (Entity entity : world.getEntities()) {
            AnimationPart animationPart = entity.getPart(AnimationPart.class);
            if (animationPart != null) {
                textureAtlas = new TextureAtlas(Gdx.files.internal(animationPart.getAtlasPath()));
                animation = new Animation(1f / 15f, textureAtlas.getRegions());
            }
        }
    }

    public void loadAssets() {

        for (Entity entity : world.getEntities()) {
            SpritePart spritePart = entity.getPart(SpritePart.class);
            if (spritePart != null) {
                GameScreen.assetManager.load(spritePart.getSpritePath(), Texture.class);
                GameScreen.assetManager.update();
            }
        }
        GameScreen.assetManager.finishLoading();

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        batch2.dispose();
        textureAtlas.dispose();
    }
}
