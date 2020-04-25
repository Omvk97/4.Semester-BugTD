package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class SpritePart implements EntityPart {
    private String spritePath;
    private float width;
    private float height;
    private int layer;
    private int alpha; // I think this ranges from 1 to 255 (from less to more transparent) (NOT ZERO!)

    public SpritePart(String spritePath, float width, float height){
        this.spritePath = spritePath;
        this.width = width;
        this.height = height;
        this.layer = 0;
        this.alpha = 1;
    }
    
    public SpritePart(String spritePath, float width, float height, int layer){
        this(spritePath, width, height);
        this.layer = layer;
    }
    
    public SpritePart(String spritePath, float width, float height, int layer, int alpha){
        this(spritePath, width, height, layer);
        this.alpha = alpha;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getLayer() {
        return layer;
    }

    public int getAlpha() {
        return alpha;
    }
    
    @Override
    public void process(GameData gameData, Entity entity) {
    }
}
