package com.unstablectrl.blooob.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.unstablectrl.blooob.gameobjects.Blob;
import com.unstablectrl.blooob.gameobjects.Box;
import com.unstablectrl.blooob.gameobjects.Circle;

public class MyContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null ) return;

        if (blobHitBox(fa, fb)) {
            Box b = (fa.getUserData() instanceof Box) ? (Box) fa.getUserData() : (Box) fb.getUserData();
            if (!b.isWall())
                b.hit();
        }

        if (blobHitCircle(fa, fb)) {
            Circle c = (fa.getUserData() instanceof Circle) ? (Circle) fa.getUserData() : (Circle) fb.getUserData();
            if (!c.isWall())
                c.hit();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean blobHitBox(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Box || b.getUserData() instanceof Box) &&
                (a.getUserData() instanceof Blob || b.getUserData() instanceof Blob);
    }

    private boolean blobHitCircle(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Circle || b.getUserData() instanceof Circle) &&
                (a.getUserData() instanceof Blob || b.getUserData() instanceof Blob);
    }
}
