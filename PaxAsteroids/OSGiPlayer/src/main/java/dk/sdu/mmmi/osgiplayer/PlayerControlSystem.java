package dk.sdu.mmmi.osgiplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.events.ShootingEvent;

public class PlayerControlSystem implements IEntityProcessingService {

    float shootingTimer = 0;
    float cooldown = 0.5f;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));

            movingPart.process(gameData, player);
            positionPart.process(gameData, player);

            if (gameData.getKeys().isDown(SPACE) && shootingTimer <= 0) {
                gameData.addEvent(new ShootingEvent(player));
                shootingTimer = cooldown;
            }
            shootingTimer -= gameData.getDelta();

            updateShape(player);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * Math.PI / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * Math.PI / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + Math.PI) * 5);
        shapey[2] = (float) (y + Math.sin(radians + Math.PI) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * Math.PI / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * Math.PI / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
