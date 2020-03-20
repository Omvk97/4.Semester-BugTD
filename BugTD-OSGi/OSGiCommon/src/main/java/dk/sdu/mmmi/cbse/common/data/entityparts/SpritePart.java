/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class SpritePart implements EntityPart {
    private String spritePath;
    private float width;
    private float height;

    public SpritePart(String spritePath, float width, float height){
        this.spritePath = spritePath;
        this.width = width;
        this.height = height;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
    }
}
