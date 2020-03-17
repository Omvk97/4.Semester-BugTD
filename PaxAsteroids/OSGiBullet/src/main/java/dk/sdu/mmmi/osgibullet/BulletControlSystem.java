package dk.sdu.mmmi.osgibullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart.EntityIdentity;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.ShootingEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        // Create new Bullets
        List<Event> eventsToDelete = new ArrayList<>();        
        for (Event event : gameData.getEvents()) {
            
            if (!(event instanceof ShootingEvent)) {
                continue;
            }
            
            eventsToDelete.add(event);
            
            Entity source = event.getSource();
            float radians = ((PositionPart) source.getPart(PositionPart.class)).getRadians();
            float x = (float) (((PositionPart) source.getPart(PositionPart.class)).getX() + 10*cos(radians));
            float y = (float) (((PositionPart) source.getPart(PositionPart.class)).getY() + 10*sin(radians));
            float deacceleration = 0;
            float acceleration = 1000000f;
            float maxSpeed = 400;
            float rotationSpeed = 0;

            Entity bullet = new Bullet();
            bullet.add(new PositionPart(x, y, radians));
            bullet.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
            bullet.add(new IdentityPart(EntityIdentity.Bullet));
            bullet.setRadius(2);
            world.addEntity(bullet);
        }
        gameData.getEvents().removeAll(eventsToDelete);

        // Handle existing Bullets
        for (Entity bullet : world.getEntities(Bullet.class)) {
            PositionPart positionPart = bullet.getPart(PositionPart.class);
            MovingPart movingPart = bullet.getPart(MovingPart.class);

            movingPart.setUp(true);

            movingPart.process(gameData, bullet);
            positionPart.process(gameData, bullet);
            
            // Delete bullet off screen
            float x = positionPart.getX();
            float y = positionPart.getY();
            if (x < 5 || x > gameData.getDisplayWidth() - 5 || y < 5 || y > gameData.getDisplayHeight() - 5) {
                world.removeEntity(bullet);
            }

            updateShape(bullet);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();
        float radius = entity.getRadius();

        shapex[0] = (float) (x - radius);
        shapey[0] = (float) (y - radius);

        shapex[1] = (float) (x - radius);
        shapey[1] = (float) (y + radius);

        shapex[2] = (float) (x + radius);
        shapey[2] = (float) (y + radius);

        shapex[3] = (float) (x + radius);
        shapey[3] = (float) (y - radius);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
