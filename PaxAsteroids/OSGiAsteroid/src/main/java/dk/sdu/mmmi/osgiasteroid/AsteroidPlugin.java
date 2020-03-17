package dk.sdu.mmmi.osgiasteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart.EntityIdentity;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SplitterPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private Entity asteroid;

    public AsteroidPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        for (int i = 0; i < 3 + new Random().nextInt(3); i++) {
            asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {

        float deacceleration = 0;
        float acceleration = 1000000f;
        float maxSpeed = 50;
        float rotationSpeed = 0;
        float x = (float) (gameData.getDisplayWidth() * (new Random().nextDouble()));
        float y = (float) (gameData.getDisplayHeight() * (new Random().nextDouble()));
        float radians = (float) (2 * Math.PI * (new Random().nextDouble()));

        Entity asteroid = new Asteroid();
        asteroid.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        asteroid.add(new PositionPart(x, y, radians));
        asteroid.add(new SplitterPart());
        asteroid.add(new IdentityPart(EntityIdentity.Asteroid));
        asteroid.setRadius(10);

        return asteroid;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity a : world.getEntities(Asteroid.class)) {
            world.removeEntity(a);
        }
    }
}
