package com.unstablectrl.blooob.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.unstablectrl.blooob.Blooob;
import com.unstablectrl.blooob.gameobjects.Blob;
import com.unstablectrl.blooob.gameobjects.Box;
import com.unstablectrl.blooob.gameobjects.Circle;
import com.unstablectrl.blooob.gameobjects.GameBox;
import com.unstablectrl.blooob.utils.InputHandler;
import com.unstablectrl.blooob.utils.MyContactListener;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private final Blooob game;

    private final int GAME_WIDTH;
    public final int GAME_HEIGHT;
    private final Vector2 GAME_CENTER;
    public static final float WORLD_SCALE = 100;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Box2DDebugRenderer debugRenderer;
    private final ShapeRenderer shapeRenderer;
    private final Sound coin;

    //    private final RayHandler rayHandler;

    private World world;

    private GameBox gameBox;
    private Circle target1;
    public float headerZero;
    private float headerWidth;
    private float headerHeight;
    public Box star1;
    public Box star2;
    public Box star3;
    private Box retry;
    private Blob blob2;
    public Blob blob;
    private Box bo2;
    private Box falling;
    public int moveCounter;
    public Vector2 retryPos;
    private ArrayList<Circle> targets;
    private int currentLevel;
    private boolean changingLevel;

    public GameScreen(Blooob game) {

        Gdx.app.debug("Screen", this.getClass().getSimpleName());

        currentLevel = 1;

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

        changingLevel = false;

        coin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));

        level1();

        Gdx.input.setInputProcessor(new InputHandler(this));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        world.step(1/60f, 6, 2);
        world.clearForces();
        camera.update();
//        falling.update();
//        bo2.update();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        debugRenderer.render(world, camera.combined.cpy().scl(WORLD_SCALE));

        shapeRenderer.setProjectionMatrix(camera.combined);
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
        game.font.getData().setScale(1f, 1f);
        game.font.draw(game.batch, String.valueOf(moveCounter), headerHeight*.8f, GAME_HEIGHT- headerHeight*.2f);
//        game.font.draw(game.batch, "3", headerHeight*.8f, GAME_HEIGHT- headerHeight*.2f);
        game.batch.end();


        if (!changingLevel){
            boolean changeLevel = true;

            for (Circle target : targets)
                if (!target.isHit())
                    changeLevel = false;
            if (changeLevel) {
                coin.play();
                nextLevel();
            }

        }
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
        coin.dispose();
    }

    private void nextLevel() {
        float delay;
        switch (++currentLevel) {
            case 1:
                delay = 2; // seconds
                changingLevel = true;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        level1();
                        changingLevel = false;
                    }
                }, delay);
                break;
            case 2:
                delay = 2; // seconds
                changingLevel = true;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        level2();
                        changingLevel = false;
                    }
                }, delay);
                break;
            default:
                delay = 2; // seconds
                changingLevel = true;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        currentLevel = 1;
                        level1();
                        changingLevel = false;
                    }
                }, delay);
                break;
        }
    }

    public void restartLevel() {
        switch (currentLevel) {
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            default:
                currentLevel = 1;
                level1();
                break;
        }
    }

    public void level1() {

        if(world != null)
            world.dispose();

        moveCounter = 0;

        world = new World(new Vector2(0, 0f), false);
        world.setContactListener(new MyContactListener());

        // Header
        Vector2 retrySize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        retryPos = new Vector2(headerWidth - (headerHeight - retrySize.x)/2 - retrySize.x*1.5f, headerZero+(headerHeight - retrySize.y)/2);
        retry = new Box(retryPos, retrySize, world, true);
        retry.setTexture("retry.png");

        Vector2 startSize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        star1 = new Box(new Vector2(headerWidth/2 - startSize.x/2 - startSize.x - (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star1.setTexture("star-full.png");

        star2 = new Box(new Vector2(headerWidth/2 - startSize.x/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star2.setTexture("star-full.png");

        star3 = new Box(new Vector2(headerWidth/2 - startSize.x/2 + startSize.x + (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star3.setTexture("star-full.png");

        // Walls
        gameBox = new GameBox(GAME_WIDTH, GAME_HEIGHT, world);

        // Static block
        bo2 = new Box(GAME_CENTER.cpy().add(-250, -250-headerHeight/2), new Vector2(500, 500), world, true);
        bo2.setTexture("square-static.png");

        // Falling block
//        Vector2 fallingSize = new Vector2(100, 100);
//        falling = new Box(GAME_CENTER.cpy().add(fallingSize.cpy().scl(-.5f)).add(-600, 0), fallingSize, world, false);
//        falling.setFixedRotation(false);
//        falling.setTexture("square-moving.png");


        // circle
        float target1Size = WORLD_SCALE * .5f;
        target1 = new Circle(GAME_CENTER.cpy().add(500,-headerHeight/2), target1Size, world, true);
        target1.setTexture("circle.png");

        targets = new ArrayList<Circle>();
        targets.add(target1);

        // Blob
        float blobSize = WORLD_SCALE * 1.0f;
        blob = new Blob(GAME_CENTER.cpy().add(-500,-headerHeight/2), blobSize, 24, world);
        blob.setTexture("watercolor_circle.png");
    }

    private void level2() {
        if(world != null)
            world.dispose();

        moveCounter = 0;

        world = new World(new Vector2(0, 0f), false);
        world.setContactListener(new MyContactListener());

        // Header
        Vector2 retrySize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        retryPos = new Vector2(headerWidth - (headerHeight - retrySize.x)/2 - retrySize.x*1.5f, headerZero+(headerHeight - retrySize.y)/2);
        retry = new Box(retryPos, retrySize, world, true);
        retry.setTexture("retry.png");

        Vector2 startSize = new Vector2(headerHeight * .8f, headerHeight * .8f);
        star1 = new Box(new Vector2(headerWidth/2 - startSize.x/2 - startSize.x - (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star1.setTexture("star-full.png");

        star2 = new Box(new Vector2(headerWidth/2 - startSize.x/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star2.setTexture("star-full.png");

        star3 = new Box(new Vector2(headerWidth/2 - startSize.x/2 + startSize.x + (headerHeight-startSize.x)/2, headerZero + headerHeight/2 - startSize.y/2), retrySize, world, true);
        star3.setTexture("star-full.png");

        // Walls
        gameBox = new GameBox(GAME_WIDTH, GAME_HEIGHT, world);

        // Static block
//        bo2 = new Box(new Vector2(GAME_WIDTH/2-60, 100), new Vector2(100, 100), world, true);
//        bo2.setTexture("square-static.png");

        // Falling block


        for (int i = 0; i < 5; i++) {
            Vector2 fallingSize = new Vector2(100, 100);
            Box breakWall = new Box(GAME_CENTER.cpy().add(fallingSize.cpy().scl(-.5f)).scl(1,0).add( 0 , 200 * i), fallingSize, world, false);
            breakWall.setFixedRotation(false);
            breakWall.setTexture("square-moving.png");
        }

        // circle
        float target1Size = WORLD_SCALE * .5f;
        target1 = new Circle(new Vector2(GAME_WIDTH-200, 100), target1Size, world, true);
        target1.setTexture("circle.png");

        Circle target2 = new Circle(new Vector2(GAME_WIDTH-200, headerZero-200), target1Size, world, true);
        target2.setTexture("circle.png");

        targets = new ArrayList<Circle>();
        targets.add(target1);
        targets.add(target2);

        // Blob
        float blobSize = WORLD_SCALE * 1.0f;
        blob = new Blob(GAME_CENTER.cpy().add(-500,-headerHeight/2), blobSize, 24, world);
        blob.setTexture("watercolor_circle.png");
    }
}
