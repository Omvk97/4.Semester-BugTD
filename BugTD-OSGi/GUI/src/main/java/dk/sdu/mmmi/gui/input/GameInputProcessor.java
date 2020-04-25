package dk.sdu.mmmi.gui.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.GameOverEvent;

public class GameInputProcessor extends InputAdapter {

    private final GameData gameData;

    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public boolean keyDown(int k) {
        if (k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, true);
        }
        if (k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, true);
        }
        if (k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, true);
        }
        if (k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);
        }
        if (k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, true);
        }
        if (k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, true);
            gameData.addEvent(new GameOverEvent(null));
        }
        if (k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, true);
        }
        if (k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if (k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, false);
        }
        if (k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, false);
        }
        if (k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, false);
        }
        if (k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, false);
        }
        if (k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, false);
        }
        if (k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, false);
        }
        if (k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, false);
        }
        if (k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, false);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = gameData.getDisplayHeight() - screenY;    // The y-value needs to be reversed for unknown reason
        gameData.addEvent(new ClickEvent(null, screenX, screenY));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenY = gameData.getDisplayHeight() - screenY;    // The y-value needs to be reversed for unknown reason
        gameData.setMouseX(screenX);
        gameData.setMouseY(screenY);
        return true;
    }
}
