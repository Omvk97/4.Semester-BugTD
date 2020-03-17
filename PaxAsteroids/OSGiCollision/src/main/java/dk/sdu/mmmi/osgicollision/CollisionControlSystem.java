package dk.sdu.mmmi.osgicollision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SplitterPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.IdentityPart.EntityIdentity;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionControlSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity2.getID().equals(entity1.getID())) {
                    continue;
                }

                if (collides(entity1, entity2)) {
                    PositionPart entity1PosPart = entity1.getPart(PositionPart.class);
                    PositionPart entity2PosPart = entity2.getPart(PositionPart.class);
                    IdentityPart entity1IdPart = entity1.getPart(IdentityPart.class);
                    IdentityPart entity2IdPart = entity2.getPart(IdentityPart.class);
                    
                    if (entity1IdPart.getIdentity() == EntityIdentity.Asteroid) {
                        if (entity2IdPart.getIdentity() == EntityIdentity.Asteroid) {
                            entity1PosPart.setRadians((float) (entity1PosPart.getRadians() + Math.PI)); // Only one of the astoroids turn at a time to prevent them from turning twice
                        } else if (entity2IdPart.getIdentity() == EntityIdentity.Player || entity2IdPart.getIdentity() == EntityIdentity.Enemy) {
                            world.removeEntity(entity2);
                        } else if (entity2IdPart.getIdentity() == EntityIdentity.Bullet) {
                            world.removeEntity(entity2);
                            ((SplitterPart) entity1.getPart(SplitterPart.class)).setShouldSplit(true);
                        }
                    }

                    if (entity1IdPart.getIdentity() == EntityIdentity.Bullet) {
                        if (entity2IdPart.getIdentity() == EntityIdentity.Enemy || entity2IdPart.getIdentity() == EntityIdentity.Player) {
                            world.removeEntity(entity1);

                            LifePart lifePart = entity2.getPart(LifePart.class);
                            lifePart.setLife(lifePart.getLife() - 25);

                            if (lifePart.getLife() <= 0) {
                                world.removeEntity(entity2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean collides(Entity entity1, Entity entity2) {
        PositionPart posPart1 = entity1.getPart(PositionPart.class);
        PositionPart posPart2 = entity2.getPart(PositionPart.class);
        float dx = (float) posPart1.getX() - (float) posPart2.getX();
        float dy = (float) posPart1.getY() - (float) posPart2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < (entity1.getRadius() + entity2.getRadius())) {
            return true;
        }
        return false;
    }
}
