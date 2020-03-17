package dk.sdu.mmmi.osgiasteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart.EntityIdentity;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SplitterPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AsteroidControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            
            SplitterPart splitterPart = asteroid.getPart(SplitterPart.class);
            if (splitterPart.ShouldSplit()) {
                world.removeEntity(asteroid);
                splitAsteroid(asteroid , world);
                continue;
            }

            PositionPart positionPart = asteroid.getPart(PositionPart.class);
            MovingPart movingPart = asteroid.getPart(MovingPart.class);
            movingPart.setUp(true);
            movingPart.process(gameData, asteroid);
            positionPart.process(gameData, asteroid);

            updateShape(asteroid);
        }
    }

    private void splitAsteroid(Entity asteroid, World world) {
        
        PositionPart posPart = asteroid.getPart(PositionPart.class);
        MovingPart movPart = asteroid.getPart(MovingPart.class);
        
        float newRadius = asteroid.getRadius() / 2;
        if (newRadius > 2) {
            // First new asteroid
            float radians = (float) (posPart.getRadians() + (0.2f * Math.PI));
            float x = (float) (posPart.getX() + 10 * cos(radians));
            float y = (float) (posPart.getY() + 10 * sin(radians));
            Asteroid newAsteroid1 = new Asteroid();
            newAsteroid1.add(movPart);
            newAsteroid1.add(new PositionPart(x, y, radians));
            newAsteroid1.add(new SplitterPart());
            newAsteroid1.add(new IdentityPart(EntityIdentity.Asteroid));
            newAsteroid1.setRadius(newRadius);

            // Second new asteroid
            radians = (float) (posPart.getRadians() + (1.8f * Math.PI));
            x = (float) (posPart.getX() + 10 * cos(radians));
            y = (float) (posPart.getY() + 10 * sin(radians));
            Asteroid newAsteroid2 = new Asteroid();
            newAsteroid2.add(movPart);
            newAsteroid2.add(new PositionPart(x, y, radians));
            newAsteroid2.add(new SplitterPart());
            newAsteroid2.add(new IdentityPart(EntityIdentity.Asteroid));
            newAsteroid2.setRadius(newRadius);

            world.addEntity(newAsteroid1);
            world.addEntity(newAsteroid2);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
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
