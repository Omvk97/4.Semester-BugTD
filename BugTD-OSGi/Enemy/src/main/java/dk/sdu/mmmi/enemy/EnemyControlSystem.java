package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.*;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonenemy.Enemy;

public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for(Entity enemy : world.getEntities(Enemy.class)){
            PositionPart p = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            AnimationPart animationPart = enemy.getPart(AnimationPart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setDown(gameData.getKeys().isDown(DOWN));
            setAnimation(enemy, gameData, animationPart);
            
            movingPart.process(gameData,enemy);
            p.process(gameData,enemy);
            
            if(((LifePart) enemy.getPart(LifePart.class)).isDead()) {
                world.removeEntity(enemy);
            }
        }
    }
    public void setAnimation(Entity enemy, GameData gameData, AnimationPart animPart) {
        if (gameData.getKeys().isDown(DOWN)) {
            animPart.setAtlasPath("texturesprites/enemy/enemydown.atlas");
        }
        if(gameData.getKeys().isDown(UP)) {
            animPart.setAtlasPath("texturesprites/enemy/enemyup.atlas");
        }
        if(gameData.getKeys().isDown(RIGHT)){
            animPart.setAtlasPath("texturesprites/enemy/enemyright.atlas");
        }
        if(gameData.getKeys().isDown(LEFT)){
            animPart.setAtlasPath("texturesprites/enemy/enemyleft.atlas");
        }
    }
}
