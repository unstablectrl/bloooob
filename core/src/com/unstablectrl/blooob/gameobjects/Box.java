package com.unstablectrl.blooob.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.unstablectrl.blooob.screens.GameScreen;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class Box {

    private boolean isWall;
    private Vector2 boxPos;
    private Vector2 boxSize;
    private boolean isStatic;
    private World world;
    private Body pBody;
    private Box2DSprite box2DSprite;
    private boolean hit;

    public Box(Vector2 pos, Vector2 size, World world, boolean isStatic) {
        this.boxPos = pos;
        this.boxSize = size;
        this.world = world;
        this.isStatic = isStatic;
        this.isWall = false;
        createBox();
        pBody.setLinearDamping(5f);
        pBody.setAngularDamping(5f);
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.pBody.setFixedRotation(fixedRotation);
    }

    private void createBox(){
        // Scale to Box2D coordinates
        Vector2 fBoxPos = boxPos.cpy().scl(1/GameScreen.WORLD_SCALE);
        Vector2 fBoxSize = boxSize.cpy().scl(1/GameScreen.WORLD_SCALE);

        // Body Definition
        BodyDef def = new BodyDef();
        def.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(fBoxPos.x + fBoxSize.x/2, fBoxPos.y + fBoxSize.y/2);
        def.fixedRotation = true;

        // Shape Definition
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fBoxSize.x/2, fBoxSize.y/2);

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
        Vector2 pos = pBody.getPosition().cpy();
        Vector2 size = boxSize.cpy().scl(1/GameScreen.WORLD_SCALE);
        boxPos = pos.add((size.scl(-.5f))).scl(GameScreen.WORLD_SCALE).cpy();
//        boxPos = pBody.getPosition().cpy().scl(worldScale);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(boxPos.x, boxPos.y, boxSize.x, boxSize.y);
    }

    public void render(SpriteBatch batch) {
        box2DSprite.draw(batch, pBody);
    }

    public void hit() {
        if (this.hit) return;
        this.hit = true;
        setTexture("badlogic.jpg");
    }


    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }
}
