package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class BulletEntityPlugin implements IGamePluginService {

    private Entity bullet;
    private float x, y, radiant;

    public BulletEntityPlugin(float x, float y, float radiant) {
        this.x = x;
        this.y = y;
        this.radiant = radiant;
    }

    private Entity createBullet(GameData gameData) {
        Random rand = new Random();
        Entity bullet = new Entity();
        bullet.setType(BULLET);
        bullet.setRadians(radiant);
        bullet.setPosition(x + ((float) Math.cos(radiant) * 20), y + ((float) Math.sin(radiant) * 20));
        bullet.setMaxSpeed(300);
        bullet.setWrap(false);
        return bullet;
    }

    @Override
    public void start(GameData gameData, World world) {
        bullet = createBullet(gameData);
        world.addEntity(bullet);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(bullet);
    }

}
