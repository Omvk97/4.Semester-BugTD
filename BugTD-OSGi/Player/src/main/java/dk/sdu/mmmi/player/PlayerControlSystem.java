package dk.sdu.mmmi.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.PlayerArrivedEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.TileSizes;

public class PlayerControlSystem implements IEntityProcessingService {

    float targetX = 100;
    float targetY = 100;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            MovingPart movingPart = player.getPart(MovingPart.class);
            AnimationPart animationPart = player.getPart(AnimationPart.class);

//            movingPart.setLeft(gameData.getKeys().isDown(GameKeys.LEFT));
//            movingPart.setRight(gameData.getKeys().isDown(GameKeys.RIGHT));
//            movingPart.setUp(gameData.getKeys().isDown(GameKeys.UP));
//            movingPart.setDown(gameData.getKeys().isDown(GameKeys.DOWN));
            setAnimation(player, gameData, animationPart);

            movePlayer(gameData, world, player, animationPart);
        }
    }

    public void movePlayer(GameData gameData, World world, Entity player, AnimationPart ani) {
        PositionPart pos = player.getPart(PositionPart.class);
        MovingPart mov = player.getPart(MovingPart.class);

        for (Event clickEvent : gameData.getEvents(ClickEvent.class)) {
            gameData.removeEvent(clickEvent);

            int clickX = ((ClickEvent) clickEvent).getX();
            int clickY = ((ClickEvent) clickEvent).getY();
            targetX = roundDown(clickX, TileSizes.GRASS_WIDTH);
            targetY = roundDown(clickY, TileSizes.GRASS_WIDTH);

            mov.setUp(false);
            mov.setDown(false);
            mov.setLeft(false);
            mov.setRight(false);
        }

        if (!isKeyPressed(gameData)) {
            float dx = targetX - pos.getX();
            float dy = targetY - pos.getY();

            // Horizontal movement
            if (dx > 0) {
                pos.setX(dx > 5 ? pos.getX() + 5 : pos.getX() + dx);
                ani.setAtlasPath("texturesprites/player32/right.atlas");
            } else if (dx < 0) {
                pos.setX(dx < -5 ? pos.getX() - 5 : pos.getX() + dx);
                ani.setAtlasPath("texturesprites/player32/left.atlas");
            }

            // Vertical movement
            if (dy > 0) {
                pos.setY(dy > 5 ? pos.getY() + 5 : pos.getY() + dy);
                ani.setAtlasPath("texturesprites/player32/stand.atlas");
            } else if (dy < 0) {
                pos.setY(dy < -5 ? pos.getY() - 5 : pos.getY() + dy);
                ani.setAtlasPath("texturesprites/player32/stand.atlas");
            }

            // Player arrived and tower can be placed
            if (pos.getX() == targetX && pos.getY() == targetY) {
                gameData.addEvent(new PlayerArrivedEvent(player));
            }
        }
    }

    public void setAnimation(Entity player, GameData gameData, AnimationPart ani) {
        if (gameData.getKeys().isDown(GameKeys.DOWN)) {
            ani.setAtlasPath("texturesprites/player32/stand.atlas");
        }
        if (gameData.getKeys().isDown(GameKeys.UP)) {
            ani.setAtlasPath("texturesprites/player32/stand.atlas");
        }
        if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
            ani.setAtlasPath("texturesprites/player32/right.atlas");
        }
        if (gameData.getKeys().isDown(GameKeys.LEFT)) {
            ani.setAtlasPath("texturesprites/player32/left.atlas");
        }
    }

    public void playerWithinRangeOfTower(World world, PositionPart pos) {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public boolean isKeyPressed(GameData gameData) {
        return gameData.getKeys().isDown(GameKeys.DOWN) || gameData.getKeys().isDown(GameKeys.UP)
                || gameData.getKeys().isDown(GameKeys.RIGHT) || gameData.getKeys().isDown(GameKeys.LEFT);
    }

    public int roundDown(double number, double place) {
        double result = number / place;
        result = Math.floor(result);
        result *= place;
        return (int) result;
    }
}
