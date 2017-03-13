package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.asteroid.AsteroidEntityPlugin;
import dk.sdu.mmmi.cbse.bullet.BulletControlSystem;
import dk.sdu.mmmi.cbse.bullet.BulletEntityPlugin;
import dk.sdu.mmmi.cbse.collision.CollisionControlSystem;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.playersystem.PlayerEntityPlugin;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import dk.sdu.mmmi.cbse.enemy.EnemyControlSystem;
import dk.sdu.mmmi.cbse.enemy.EnemyEntityPlugin;
import dk.sdu.mmmi.cbse.wrap.WrapControlSystem;
import dk.sdu.mmmi.cbse.common.util.SPILocator;
import java.util.ArrayList;
import java.util.List;


public class Game implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer sr;

    private final GameData gameData = new GameData();
    private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    private World world = new World();
    private IGamePluginService playerPlugin, enemyPlugin, asteroidPlugin, bulletPlugin;
    private IEntityProcessingService playerProcessor;
    private IEntityProcessingService enemyProcessor;
    private IEntityProcessingService asteroidProcessor;
    private IEntityProcessingService bulletProcessor;
    private IEntityProcessingService wrapProcessor;
    private IEntityProcessingService collisionProcessor;

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(
                new GameInputProcessor(gameData)
        );

        playerPlugin = new PlayerEntityPlugin();
        playerPlugin.start(gameData, world);
        playerProcessor = new PlayerControlSystem();
        entityProcessors.add(playerProcessor);

        enemyPlugin = new EnemyEntityPlugin();
        enemyPlugin.start(gameData, world);
        enemyProcessor = new EnemyControlSystem();
        entityProcessors.add(enemyProcessor);

        SPILocator.locateAll(IGamePluginService.class);
        
        for(IGamePluginService x : SPILocator.locateAll(IGamePluginService.class)) {
            x.start(gameData, world);
        }
        
        

        
        
        
        bulletProcessor = new BulletControlSystem();
        entityProcessors.add(bulletProcessor);

        wrapProcessor = new WrapControlSystem();
        entityProcessors.add(wrapProcessor);

        collisionProcessor = new CollisionControlSystem();
        entityProcessors.add(collisionProcessor);

    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        draw();

        gameData.getKeys().update();
    }

    private void addShootEvent(Event e) {
        bulletPlugin = new BulletEntityPlugin(world.getEntityMap().get(e.getEntityID()).getX(), world.getEntityMap().get(e.getEntityID()).getY(), world.getEntityMap().get(e.getEntityID()).getRadians());
        bulletPlugin.start(gameData, world);
        gameData.removeEvent(e);
    }

    private void update() {
        
        for(IEntityProcessingService x : SPILocator.locateAll(IEntityProcessingService.class)) {
            x.process(gameData, world);
        }
        
        // Update
        for (IEntityProcessingService eps : entityProcessors) {
            eps.process(gameData, world);
        }

        for (Event e : gameData.getEvents()) {

            if (e.getType() == EventType.PLAYER_SHOOT) {
                addShootEvent(e);
            }

            if (e.getType() == EventType.ENEMY_SHOOT) {
                addShootEvent(e);
            }
        }

    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();
            if (shapex != null && shapey != null) {

                if (entity.getType() == PLAYER) {
                    sr.setColor(1, 1, 1, 1);
                }

                if (entity.getType() == ENEMY) {
                    sr.setColor(Color.RED);
                }

                if (entity.getType() == ASTEROIDS) {
                    sr.setColor(Color.CYAN);
                }

                if (entity.getType() == BULLET) {
                    sr.setColor(Color.ORANGE);
                }

                sr.begin(ShapeRenderer.ShapeType.Line);

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {

                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }
                sr.end();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
