package dk.sdu.mmmi.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import dk.sdu.mmmi.cbse.Game;
import dk.sdu.mmmi.gui.GuiPluginService;
import dk.sdu.mmmi.gui.components.LabelFactory;
import dk.sdu.mmmi.gui.components.TextButtonFactory;

public class MenuScreen implements Screen {

    private Stage stage;

    public MenuScreen() {
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        // table.setDebug(true);
        stage.addActor(table);

        Label title = new LabelFactory("Bug TD", 2).create();
        Label flashMessage = new LabelFactory(Game.getInstance().getGameData().getMenuFlashMessage(), 1).create();
        TextButton easyGame = new TextButtonFactory("Easy").create();
        TextButton mediumGame = new TextButtonFactory("Medium").create();
        TextButton hardGame = new TextButtonFactory("Antpossible!").create();
        TextButton exit = new TextButtonFactory("Exit").create();
        Image queenImage = new Image(new Texture("menu/queen.png"));
        queenImage.setScale(2);
        queenImage.setOriginX(16); // Scale from center of image (32x32)

        table.add(queenImage);

        table.row().padBottom(20);
        table.add(title);

        table.row().padBottom(20);
        table.add(flashMessage);

        table.row();
        table.add(easyGame);

        table.row();
        table.add(mediumGame);

        table.row();
        table.add(hardGame);


        table.row().padTop(10);
        table.add(exit);

        // Create button listeners
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        easyGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Game.getInstance().getGameData().setDifficulty(1);
                GuiPluginService.getInstance().startGame();
            }
        });

        mediumGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Game.getInstance().getGameData().setDifficulty(2);
                GuiPluginService.getInstance().startGame();
            }
        });

        hardGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Game.getInstance().getGameData().setDifficulty(3);
                GuiPluginService.getInstance().startGame();
            }
        });

    }

    @Override
    public void render(float v) {
        // clear the screen ready for next set of images to be drawn
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(57f/255f, 130f/255f, 75f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
    }
}
