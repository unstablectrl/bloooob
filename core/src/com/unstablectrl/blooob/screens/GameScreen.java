package com.unstablectrl.blooob.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.unstablectrl.blooob.Blooob;
import com.unstablectrl.blooob.gameobjects.Blob;
import com.unstablectrl.blooob.gameobjects.Box;
import com.unstablectrl.blooob.gameobjects.Circle;
import com.unstablectrl.blooob.gameobjects.GameBox;
import com.unstablectrl.blooob.utils.InputHandler;
import com.unstablectrl.blooob.utils.MyContactListener;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class GameScreen implements Screen {

    private final Blooob game;

    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;
    private final Vector2 GAME_CENTER;
    public static final float WORLD_SCALE = 100;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Box2DDebugRenderer debugRenderer;
    private final ShapeRenderer shapeRenderer;

    //    private final RayHandler rayHandler;

    private World world;

    private final GameBox gameBox;
    private final Circle target1;
    private float headerZero;
    private float headerWidth;
    private float headerHeight;
    private Box star1;
    private Box star2;
    private Box star3;
    private Box retry;
    private Blob blob2;
    private Blob blob;
    private Box bo2;
    private Box falling;
    private int moveCounter;

    public GameScreen(Blooob game) {

        Gdx.app.debug("Screen", this.getClass().getSimpleName());

        this.game = game;

        GAME_WIDTH = 1920;
        GAME_HEIGHT = 1080;
        GAME_CENTER = new Vector2(GAME_WIDTH/2, GAME_HEIGHT/2);

        moveCounter = 0;

        float headerSpacingScale = .15f;
        headerHeight = GAME_HEIGHT * headerSpacingScale;
        headerWidth = GAME_WIDTH;
        headerZero = GAME_HEIGHT- headerHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(GAME_WIDTH, GAME_HEIGHT, camera);

        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();


        world = new World(new Vector2(0, 0f), false);
        world.setContactListener(new MyContactListener());

        // Header
        Vector2 retrySize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        retry = new Box(new Vector2(headerWidth - (headerHeight - retrySize.x)/2 - retrySize.x*1.5f, headerZero+(headerHeight - retrySize.y)/2), retrySize, world, true);
        retry.setTexture("retry.png");

        Vector2 startSize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        star1 = new Box(new Vector2(headerWidth/2 - startSize.x/2 - startSize.x - (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star1.setTexture("star.png");

        star2 = new Box(new Vector2(headerWidth/2 - startSize.x/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star2.setTexture("star.png");

        star3 = new Box(new Vector2(headerWidth/2 - startSize.x/2 + startSize.x + (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star3.setTexture("star.png");

        // Walls
        gameBox = new GameBox(GAME_WIDTH, GAME_HEIGHT, world);

        // Static block
        bo2 = new Box(new Vector2(GAME_WIDTH/2-60, 100), new Vector2(100, 100), world, true);
        bo2.setTexture("red-transparent.png");

        // Falling block
        Vector2 fallingSize = new Vector2(100, 100);
        falling = new Box(GAME_CENTER.cpy().add(fallingSize.cpy().scl(-.5f)).add(-600, 0), fallingSize, world, false);
        falling.setFixedRotation(false);
        falling.setTexture("red-transparent.png");

        // circle
        float target1Size = WORLD_SCALE * .5f;
        target1 = new Circle(new Vector2(GAME_WIDTH-200, 100), target1Size, world, true);
        target1.setTexture("playstation_circle.png");

        // Blob
        float blobSize = WORLD_SCALE * 1.0f;
        blob = new Blob(GAME_CENTER.cpy(), blobSize, 24, world);
        blob.setTexture("watercolor_circle.png");

        Gdx.input.setInputProcessor(new InputHandler(blob));
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        world.step(1/60f, 6, 2);
        world.clearForces();
        camera.update();
        falling.update();
        bo2.update();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        debugRenderer.render(world, camera.combined.cpy().scl(WORLD_SCALE));

//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(1, 1, 0, 1);
//        gameBox.render(shapeRenderer);
//        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.polygon(blob.getFBonesPos());
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined.cpy().scl(WORLD_SCALE));
        game.batch.begin();
        Box2DSprite.draw(game.batch, world);
        game.batch.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.getData().setScale(.7f, .7f);
        game.font.draw(game.batch, String.valueOf(moveCounter), headerHeight*.8f, GAME_HEIGHT- headerHeight*.2f);
        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {
        viewport.getCamera().position.set(this.GAME_WIDTH/2, this.GAME_HEIGHT/2, 0);
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        shapeRenderer.dispose();
        world.dispose();
    }
}
