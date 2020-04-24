package dk.sdu.mmmi.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonFactory {

    String text;

    public TextButtonFactory(String text) {
        this.text = text;
    }

    public TextButton create() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        style.font = font;

        return new TextButton(text, style);
    }
}
