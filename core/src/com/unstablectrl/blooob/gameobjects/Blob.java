package com.unstablectrl.blooob.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.unstablectrl.blooob.screens.GameScreen;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.ArrayList;

public class Blob {

    private Vector2 blobPos;
    private World world;
    private final float WORLD_SCALE = GameScreen.WORLD_SCALE;
    private ArrayList<Body> fBonesInn = new ArrayList<Body>();
    private ArrayList<Body> fBones = new ArrayList<Body>();
    private int boneCount;
    private float fBoneSize; // 4 cm diameter
    private float fBoneSizeMain; // 8 cm diameter
    private float fBoneDensity = 1;
    private float fBoneRestitution = 0.0f;
    private float fBoneFriction = 1.0f;
    private float blobSize;
    private Body fBoneMain;

    public Blob(Vector2 pos, float size, int vertexCount, World world) {
        this.blobPos = pos;
        this.blobSize = size;
        this.boneCount = vertexCount;
        this.world = world;
        this.fBoneSize = size/ WORLD_SCALE * 0.08f;
        this.fBoneSizeMain = size/ WORLD_SCALE * 0.9f;

        createBlob();
        fBoneMain.setLinearDamping(.1f);
//        fBoneMain.setAngularDamping(.01f);
        for (Body bone : fBones)
            bone.setLinearDamping(1f);
        for (Body bone : fBonesInn)
            bone.setLinearDamping(1f);
    }

    public Blob(float x, float y, float size, int vertexCount, World world) {
        this(new Vector2(x, y), size, vertexCount, world);
    }

    public Blob(Vector2 pos, float size, World world) {
        this(pos, size, 30, world);
    }

    public Blob(float x, float y, float size, World world) {
        this(x, y, size, 30, world);
    }

    public void createBlob() {

        Vector2 fBlobPos = blobPos.scl(1/ WORLD_SCALE);
        float fBlobSize = blobSize/ WORLD_SCALE;

        fBoneMain = bone(fBlobPos, fBoneSizeMain);

        for (int i = 0; i < boneCount; i++) {
            // Calculate bone position relative to blob centroid
            double angle = (2*Math.PI) / boneCount * i;
            Vector2 fBoneRelPos = new Vector2(
                    (float) (fBlobSize/2 *.4 * Math.cos(angle)),
                    (float) (fBlobSize/2 *.4 * Math.sin(angle)));
            Vector2 fBonePos = fBlobPos.cpy().add(fBoneRelPos);

            // Create first layer Bone
            fBonesInn.add(bone(fBonePos, fBoneSize));

            // Create distance joint between first layer outer bones and main bone
            DistanceJointDef dJoint = new DistanceJointDef();
            dJoint.bodyA = fBoneMain;
            dJoint.bodyB = fBonesInn.get(fBonesInn.size()-1);
            dJoint.localAnchorA.set(new Vector2(0,0));
            dJoint.localAnchorB.set(new Vector2(0,0));
            dJoint.length = fBlobSize/2;
            dJoint.dampingRatio = 0.0f;
            dJoint.frequencyHz = 30;
            world.createJoint(dJoint);

            // Calculate second layer bone position relative to blob centroid
            fBoneRelPos = new Vector2(
                    (float) (fBlobSize/2 * Math.cos(angle)),
                    (float) (fBlobSize/2 * Math.sin(angle)));
            fBonePos = fBlobPos.cpy().add(fBoneRelPos);

            // Create second layer Bone
            fBones.add(bone(fBonePos, fBoneSize));

            // Create distance joint between first layer and second layer bones
            dJoint = new DistanceJointDef();
            dJoint.bodyA = fBones.get(fBones.size()-1);
            dJoint.bodyB = fBonesInn.get(fBonesInn.size()-1);
//            dJoint.bodyB = fBoneMain;
            dJoint.localAnchorA.set(new Vector2(0,0));
            dJoint.localAnchorB.set(new Vector2(0,0));
            dJoint.length = fBonesInn.get(fBonesInn.size()-1).getPosition().dst(fBones.get(fBones.size()-1).getPosition());
//            dJoint.length = fBlobSize/2;
            dJoint.dampingRatio = 0.0f;
            dJoint.frequencyHz = 30;
            world.createJoint(dJoint);

            // Create distance joint between outer bones
            dJoint.dampingRatio = 0.0f;
            dJoint.frequencyHz = 30;
            if (i > 0) {
                // 1st layer
                dJoint.bodyA = fBonesInn.get(fBonesInn.size()-2);
                dJoint.bodyB = fBonesInn.get(fBonesInn.size()-1);
                dJoint.length = fBonesInn.get(fBonesInn.size()-2).getPosition().dst(fBonesInn.get(fBonesInn.size()-1).getPosition());
                world.createJoint(dJoint);

                // 2nd to 1st layer
                dJoint.bodyA = fBones.get(fBones.size()-1);
                dJoint.bodyB = fBonesInn.get(fBonesInn.size()-2);
                dJoint.length = fBones.get(fBones.size()-1).getPosition().dst(fBonesInn.get(fBonesInn.size()-2).getPosition());
                world.createJoint(dJoint);

                dJoint.bodyA = fBonesInn.get(fBonesInn.size()-1);
                dJoint.bodyB = fBones.get(fBones.size()-2);
                dJoint.length = fBonesInn.get(fBonesInn.size()-1).getPosition().dst(fBones.get(fBones.size()-2).getPosition());
                world.createJoint(dJoint);

                // 2nd layer
                dJoint.bodyA = fBones.get(fBones.size()-2);
                dJoint.bodyB = fBones.get(fBones.size()-1);
                dJoint.length = fBones.get(fBones.size()-2).getPosition().dst(fBones.get(fBones.size()-1).getPosition());
                world.createJoint(dJoint);

            }
            if(i == boneCount-1) {
                // 1st layer
                dJoint.bodyA = fBonesInn.get(0);
                dJoint.bodyB = fBonesInn.get(fBonesInn.size()-1);
                dJoint.length = fBonesInn.get(0).getPosition().dst(fBonesInn.get(fBonesInn.size()-1).getPosition());
                world.createJoint(dJoint);

                // 2nd to 1st layer
                dJoint.bodyA = fBonesInn.get(fBonesInn.size()-1);
                dJoint.bodyB = fBones.get(0);
                dJoint.length = fBonesInn.get(fBonesInn.size()-1).getPosition().dst(fBones.get(0).getPosition());
                world.createJoint(dJoint);

                // 2nd layer
                dJoint.bodyA = fBones.get(0);
                dJoint.bodyB = fBones.get(fBones.size()-1);
                dJoint.length = fBones.get(0).getPosition().dst(fBones.get(fBones.size()-1).getPosition());
                world.createJoint(dJoint);
            }
        }
    }

    private Body bone(Vector2 pos, float size) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(size/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = fBoneDensity;
        fixtureDef.restitution = fBoneRestitution;
        fixtureDef.friction = fBoneFriction;

        Body theSphere = world.createBody(bodyDef);
        theSphere.createFixture(fixtureDef).setUserData(this);

        return theSphere;
    }

    public void moveVertical(float vf) {
        fBoneMain.setLinearVelocity(fBoneMain.getLinearVelocity().x, vf);
    }

    public void moveHorizontal(float hf) {
        fBoneMain.setLinearVelocity(hf, fBoneMain.getLinearVelocity().y);
    }

    public void push(Vector2 force) {
//        fBoneMain.applyForceToCenter(force, false);
        fBoneMain.setLinearVelocity(force.x, force.y);
    }

    public float[] getFBonesPos() {
        float vertices[] = new float[fBones.size()*2];

        for (int i = 0; i < fBones.size(); i++) {
            Body bone = fBones.get(i);
            Vector2 bonePos = bone.getPosition().cpy().scl(WORLD_SCALE);
            vertices[i*2] = bonePos.x;
            vertices[i*2+1] = bonePos.y;
        }
        return vertices;
    }

    public Vector2 getFBoneMainPos() {
        return fBoneMain.getPosition();
    }

    public void setTexture(String texturePath) {
        fBoneMain.setUserData(new Box2DSprite(new Texture(Gdx.files.internal(texturePath))));
        for (Body bone : fBones)
            bone.setUserData(new Box2DSprite(new Texture(Gdx.files.internal(texturePath))));
//        for (Body bone : fBonesInn)
//            bone.setUserData(new Box2DSprite(new Texture(Gdx.files.internal(texturePath))));
    }


}
