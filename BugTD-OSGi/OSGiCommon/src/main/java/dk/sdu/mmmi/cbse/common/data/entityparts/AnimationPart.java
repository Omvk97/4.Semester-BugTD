package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;


public class AnimationPart implements EntityPart{
    private String atlasPath;
    private float width;
    private float height;
    private int layer;

    public AnimationPart(String atlasPath, float width, float height) {
        this.atlasPath = atlasPath;
        this.width = width;
        this.height = height;
    }
    public AnimationPart(String atlasPath, float width, float height, int layer) {
        this.atlasPath = atlasPath;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public void setAtlasPath(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
       
    }
}
