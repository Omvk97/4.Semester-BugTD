package dk.sdu.mmmi.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LabelFactory {

    String text;
    float scale = 1;

    public LabelFactory(String text) {
        this.text = text;
    }

    public LabelFactory(String text, float scale) {
        this(text);
        this.scale = scale;
    }

    public Label create() {
        Label.LabelStyle style = new Label.LabelStyle();
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(scale);
        style.font = font;

        return new Label(text, style);
    }
}
