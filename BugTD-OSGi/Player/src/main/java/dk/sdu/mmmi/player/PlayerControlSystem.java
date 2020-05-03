package dk.sdu.mmmi.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.PlayerArrivedEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.commonmap.TileSizes;
import dk.sdu.mmmi.commonplayer.Player;
import java.util.List;

public class PlayerControlSystem implements IEntityProcessingService {

    private Player player;
    private float targetX;
    private float targetY;
    private int speed = 5;

    @Override
    public void process(GameData gameData, World world) {
        // Set player
        if (player == null || !world.getEntities().contains(player)) {
            List<Entity> playersInGame = world.getEntities(Player.class);
            if (playersInGame.isEmpty()) {
                return;
            }
            player = (Player) world.getEntities(Player.class).get(0);
        }
        
        // Listen for clicks
        for (Event clickEvent : gameData.getEvents(ClickEvent.class)) {
            gameData.removeEvent(clickEvent);
            int clickX = ((ClickEvent) clickEvent).getX();
            int clickY = ((ClickEvent) clickEvent).getY();
            targetX = roundDown(clickX, TileSizes.GRASS_WIDTH);
            targetY = roundDown(clickY, TileSizes.GRASS_WIDTH);
            player.setHasTarget(true);
        }

        movePlayer(gameData, world);

        // Player has arrived and a tower can be placed
        PositionPart posPart = player.getPart(PositionPart.class);
        if (player.hasTarget() && posPart.getX() == targetX && posPart.getY() == targetY) {
            gameData.addEvent(new PlayerArrivedEvent(player, (int) posPart.getX(), (int) posPart.getY()));
            // I used to set player.hasTarget to false here, but i figured that it might as well
            // keep trying to place a tower where i stands in case an Enemy is blocking it currently.
            // Now a tower will be placed when the Enemy is gone (unless the player is moved elsewhere).
        }
    }

    public void movePlayer(GameData gameData, World world) {
        PositionPart posPart = player.getPart(PositionPart.class);

        if (isKeyPressed(gameData)) { // Movement from arrow keys
            player.setHasTarget(false);  // Cancel any current tower placement

            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                moveHorizontal(player, speed);
            }
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                moveHorizontal(player, -speed);
            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                moveVertical(player, speed);
            }
            if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                moveVertical(player, -speed);
            }
        } else if (player.hasTarget()) { // Automatic movement
            float dx = targetX - posPart.getX();
            float dy = targetY - posPart.getY();

            // Horizontal movement
            if (dx > 0) {
                moveHorizontal(player, dx > speed ? speed : dx);
            } else {
                moveHorizontal(player, dx < -speed ? -speed : dx);
            }
            // Vertical movement
            if (dy > 0) {
                moveVertical(player, dy > speed ? speed : dy);
            } else {
                moveVertical(player, dy < -speed ? -speed : dy);
            }
        }
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

    private void moveHorizontal(Entity e, float distance) {
        PositionPart posPart = e.getPart(PositionPart.class);
        posPart.setX(posPart.getX() + distance);

        if (distance > 0) {
            ((AnimationPart) e.getPart(AnimationPart.class)).setAtlasPath("texturesprites/player32/right.atlas");
        } else {
            ((AnimationPart) e.getPart(AnimationPart.class)).setAtlasPath("texturesprites/player32/left.atlas");
        }
    }

    private void moveVertical(Entity e, float speed) {
        PositionPart posPart = e.getPart(PositionPart.class);
        posPart.setY(posPart.getY() + speed);
        ((AnimationPart) e.getPart(AnimationPart.class)).setAtlasPath("texturesprites/player32/stand.atlas");
    }
}
