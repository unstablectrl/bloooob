package com.unstablectrl.blooob.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.unstablectrl.blooob.screens.GameScreen;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class Circle {

    private boolean isWall;
    private Vector2 circlePos;
    private float circleSize;
    private boolean isStatic;
    private World world;
    private Body pBody;
    private Box2DSprite box2DSprite;
    private boolean hit;

    public Circle(Vector2 pos, float size, World world, boolean isStatic) {
        this.circlePos = pos;
        this.circleSize = size;
        this.world = world;
        this.isStatic = isStatic;
        this.isWall = false;
        createCircle();
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.pBody.setFixedRotation(fixedRotation);
    }

    private void createCircle(){
        // Scale to Box2D coordinates
        Vector2 fCirclePos = circlePos.cpy().scl(1/GameScreen.WORLD_SCALE);
        float fCircleSize = circleSize/GameScreen.WORLD_SCALE;

        // Body Definition
        BodyDef def = new BodyDef();
        def.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(fCirclePos.x, fCirclePos.y);
        def.fixedRotation = true;

        // Shape Definition
        CircleShape shape = new CircleShape();
        shape.setRadius(fCircleSize);

        // Create Body and Fixture
        pBody = world.createBody(def);
        pBody.createFixture(shape, 1.0f).setUserData(this);
        shape.dispose();
    }

    public void setTexture(String texturePath) {
        box2DSprite = new Box2DSprite(new Texture(Gdx.files.internal(texturePath)));
        pBody.setUserData(box2DSprite);
    }

    public void update() {
        circlePos = pBody.getPosition().cpy().scl(1/GameScreen.WORLD_SCALE);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(circlePos.x, circlePos.y, circleSize);
    }

    public void render(SpriteBatch batch) {
        box2DSprite.draw(batch, pBody);
    }

    public void hit() {
        if (this.hit) return;
        this.hit = true;
        setTexture("circle-hit.png");
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }
}

