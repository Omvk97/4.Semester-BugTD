package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ShootingEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Random;

public class EnemyProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);

            Random random = new Random();
            movingPart.setLeft(random.nextBoolean());
            movingPart.setRight(random.nextBoolean());
            movingPart.setUp(random.nextBoolean());

            if (random.nextInt(50) == 0) {
                gameData.addEvent(new ShootingEvent(enemy));
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);

            updateShape(enemy);
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
