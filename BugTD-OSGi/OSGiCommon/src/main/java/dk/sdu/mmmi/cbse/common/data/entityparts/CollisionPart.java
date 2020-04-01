package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class CollisionPart implements EntityPart {

    private float height;
    private float width;

    public CollisionPart(float height, float width) {
        this.height = height;
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }

    private static class Dimensions {
        public float x;
        public float y;
        public float width;
        public float height;

        public Dimensions(Entity entity) throws Exception {
            PositionPart positionPart = entity.getPart(PositionPart.class);
            CollisionPart collisionPart = entity.getPart(CollisionPart.class);
            if (positionPart == null || collisionPart == null) {
                throw new Exception("MISSING PARTS! On a collision check there is a missing position- or collision-part. ");
            }
            x = positionPart.getX();
            y = positionPart.getY();
            width = collisionPart.getWidth();
            height = collisionPart.getHeight();
        }
    }

    public static boolean collides(Entity e1, Entity e2)  {
        try {
            Dimensions d1 = new Dimensions(e1);
            Dimensions d2 = new Dimensions(e2);

            if (collidesOnAxis(d1.x, d1.x + d1.width, d2.x, d2.x + d2.width)
                    && collidesOnAxis(d1.y, d1.y + d1.height, d2.y, d2.y + d2.height)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean collidesOnAxis(float start1, float end1, float start2, float end2) {
        return (start1 <= end2 && end1 > start2);
    }
}
