package com.unstablectrl.blooob.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.unstablectrl.blooob.gameobjects.Blob;
import com.unstablectrl.blooob.screens.GameScreen;

public class InputHandler implements InputProcessor {
    private Blob blob;
    private float WORLD_SCALE = GameScreen.WORLD_SCALE;
    private Vector2 pointA;
    private Vector2 pointB;

    public InputHandler(Blob blob) {
        this.blob = blob;
    }

    @Override
    public boolean keyDown(int keycode) {
        int horizontalForce = 0;
        switch (keycode) {
            case Input.Keys.LEFT:
                horizontalForce -= 1;
                break;
            case Input.Keys.RIGHT:
                horizontalForce += 1;
                break;
            case Input.Keys.UP:
                blob.moveVertical(5);
                break;
            case Input.Keys.DOWN:
                blob.moveVertical(-5);
                break;
            default:
                break;
        }
        blob.moveHorizontal(horizontalForce*5);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pointA = new Vector2(screenX, screenY);
//        Gdx.app.debug("touchDown", String.valueOf(pointA));
//        if (screenX > blob.getFBoneMainPos().scl(WORLD_SCALE).x)
//            blob.push(new Vector2(5,10));
//        else
//            blob.push(new Vector2(-5,10));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointA != null) {
            pointB = new Vector2(screenX, screenY);
            Vector2 force = pointA.add(pointB.scl(-1));
            force.scl(0.05f).scl(-1, 1);
            blob.push(force);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
