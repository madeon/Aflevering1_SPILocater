package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class EnemyEntityPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyEntityPlugin() {

    }

    private Entity createEnemyShip(GameData gameData) {
        Random rand = new Random();
        Entity enemyShip = new Entity();
        enemyShip.setType(ENEMY);
        enemyShip.setPosition(rand.nextInt(gameData.getDisplayWidth()), rand.nextInt(gameData.getDisplayHeight()));
        enemyShip.setMaxSpeed(30);
        enemyShip.setAcceleration(10);
        enemyShip.setDeacceleration(10);

        enemyShip.setRadians(3.1415f / 2);
        enemyShip.setRotationSpeed(3);

        return enemyShip;
    }

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }

}
