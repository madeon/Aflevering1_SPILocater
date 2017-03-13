package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService {

    public void drawBullet(Entity e) {
        int size = 2;
        
        //Draw the bullet
        e.setShapeX(new float[]{
            e.getX() + (float) (Math.cos(e.getRadians()) * 8) / size,
            e.getX() + (float) (Math.cos(e.getRadians() - 4 * 3.1415f / 5) * 8) / size,
            e.getX() + (float) (Math.cos(e.getRadians() + 3.1415f) * 5) / size,
            e.getX() + (float) (Math.cos(e.getRadians() + 4 * 3.1415f / 5) * 8) / size});

        e.setShapeY(new float[]{
            e.getY() + (float) (Math.sin(e.getRadians()) * 8) / size,
            e.getY() + (float) (Math.sin(e.getRadians() - 4 * 3.1415f / 5) * 8) / size,
            e.getY() + (float) (Math.sin(e.getRadians() + 3.1415f) * 5) / size,
            e.getY() + (float) (Math.sin(e.getRadians() + 4 * 3.1415f / 5) * 8) / size});
    }

    public void moveBullet(Entity e, GameData gameData) {

        //make bullet move
        e.setDx((float) (Math.cos(e.getRadians()) * e.getMaxSpeed()));
        e.setDy((float) (Math.sin(e.getRadians()) * e.getMaxSpeed()));

        //Set position
        e.setX(e.getX() + e.getDx() * gameData.getDelta());
        e.setY(e.getY() + e.getDy() * gameData.getDelta());
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities(BULLET)) {
            drawBullet(e);
            moveBullet(e, gameData);
        }
    }
}
