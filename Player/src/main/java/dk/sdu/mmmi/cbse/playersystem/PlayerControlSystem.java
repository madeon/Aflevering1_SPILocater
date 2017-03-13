package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 *
 * @author jcs
 */
public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        // TODO: Implement entity processor
        for (Entity e : world.getEntities(PLAYER)) {

            //Shoot
            if (gameData.getKeys().isPressed(GameKeys.SPACE)) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, e.getID()));
            }

            //Turn left
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                e.setRadians(e.getRadians() + e.getRotationSpeed() * gameData.getDelta());
            }

            //Turn right
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                e.setRadians(e.getRadians() - e.getRotationSpeed() * gameData.getDelta());
            }

            //Speed up
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                e.setDx((float) (e.getDx() + Math.cos(e.getRadians()) * e.getAcceleration() * gameData.getDelta()));
                e.setDy((float) (e.getDy() + Math.sin(e.getRadians()) * e.getAcceleration() * gameData.getDelta()));
            }

            //Slow down
            if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                e.setDx((float) (e.getDx() - Math.cos(e.getRadians()) * e.getAcceleration() * gameData.getDelta()));
                e.setDy((float) (e.getDy() - Math.sin(e.getRadians()) * e.getAcceleration() * gameData.getDelta()));
            }

            //Deacceleration
            float vec = (float) Math.sqrt(e.getDx() * e.getDx() + e.getDy() * e.getDy());
            if (vec > 0) {
                e.setDx(e.getDx() - (e.getDx() / vec) * e.getDeacceleration() * gameData.getDelta());
                e.setDy(e.getDy() - (e.getDy() / vec) * e.getDeacceleration() * gameData.getDelta());
            }

            if (vec > e.getMaxSpeed()) {
                e.setDx((e.getDx() / vec) * e.getMaxSpeed());
                e.setDy((e.getDy() / vec) * e.getMaxSpeed());
            }

            //Set position
            e.setX(e.getX() + e.getDx() * gameData.getDelta());
            e.setY(e.getY() + e.getDy() * gameData.getDelta());

            //set the shape for the playership
            e.setShapeX(new float[]{
                e.getX() + (float) (Math.cos(e.getRadians()) * 8),
                e.getX() + (float) (Math.cos(e.getRadians() - 4 * 3.1415f / 5) * 8),
                e.getX() + (float) (Math.cos(e.getRadians() + 3.1415f) * 5),
                e.getX() + (float) (Math.cos(e.getRadians() + 4 * 3.1415f / 5) * 8)});

            e.setShapeY(new float[]{
                e.getY() + (float) (Math.sin(e.getRadians()) * 8),
                e.getY() + (float) (Math.sin(e.getRadians() - 4 * 3.1415f / 5) * 8),
                e.getY() + (float) (Math.sin(e.getRadians() + 3.1415f) * 5),
                e.getY() + (float) (Math.sin(e.getRadians() + 4 * 3.1415f / 5) * 8)});
        }

    }
}
