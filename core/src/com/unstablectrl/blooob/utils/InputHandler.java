package com.unstablectrl.blooob.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.unstablectrl.blooob.screens.GameScreen;

public class InputHandler implements InputProcessor {
    private GameScreen gameScreen;
    private float WORLD_SCALE = GameScreen.WORLD_SCALE;
    private Vector2 pointA;
    private Vector2 pointB;

    public InputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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
                gameScreen.blob.moveVertical(5);
                break;
            case Input.Keys.DOWN:
                gameScreen.blob.moveVertical(-5);
                break;
            case Input.Keys.R:
                gameScreen.level1();
            default:
                break;
        }
        if (horizontalForce != 0)
            gameScreen.blob.moveHorizontal(horizontalForce*5);
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
//        Gdx.app.debug("touchDown screenY", String.valueOf((Gdx.graphics.getHeight()-screenY) * gameScreen.GAME_HEIGHT/  Gdx.graphics.getHeight() ));
//        Gdx.app.debug("touchDown headerZero", String.valueOf(gameScreen.headerZero));
        pointA = new Vector2(screenX, screenY);
        if ((Gdx.graphics.getHeight()-screenY) * gameScreen.GAME_HEIGHT/  Gdx.graphics.getHeight() > gameScreen.headerZero)
            pointA = null;
        if (((Gdx.graphics.getHeight()-screenY) * gameScreen.GAME_HEIGHT / Gdx.graphics.getHeight() > gameScreen.headerZero) &&
                ((screenX * gameScreen.GAME_HEIGHT / Gdx.graphics.getHeight()) > gameScreen.retryPos.x))
                gameScreen.restartLevel();
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
            if (pointB.dst(pointA) > 0f) {
                Vector2 force = pointA.add(pointB.scl(-1));
                force.scl(0.05f).scl(-1, 1);
                gameScreen.blob.push(force);
                gameScreen.moveCounter++;
                if (gameScreen.moveCounter == 5)
                    gameScreen.star3.setTexture("star-empty.png");
                if (gameScreen.moveCounter == 7)
                    gameScreen.star2.setTexture("star-empty.png");
                if (gameScreen.moveCounter == 10)
                    gameScreen.star1.setTexture("star-empty.png");
            }
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
