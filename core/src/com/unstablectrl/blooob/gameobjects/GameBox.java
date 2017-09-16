package com.unstablectrl.blooob.gameobjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class GameBox {

    private final ArrayList<Box> walls;
    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;
    private Box ground;
    private Box wallLeft;
    private Box wallRight;
    private Box ceiling;


    public GameBox(int gameWidth, int gameHeight, World world) {
        this.GAME_WIDTH = gameWidth;
        this.GAME_HEIGHT = gameHeight;

        float wallSpacingScale = .0025f;
        float wallSizeScale = .01f;
        float headerSpacingScale = .15f;

        ground = new Box(new Vector2(GAME_WIDTH*wallSpacingScale, GAME_HEIGHT*wallSpacingScale), new Vector2(GAME_WIDTH-(GAME_WIDTH*wallSpacingScale)*2, GAME_HEIGHT*wallSizeScale), world, true);
        ground.setTexture("separator-bottom.png");
        wallLeft = new Box(new Vector2(GAME_WIDTH*wallSpacingScale, GAME_HEIGHT*wallSpacingScale), new Vector2(GAME_WIDTH*wallSizeScale, GAME_HEIGHT-(GAME_HEIGHT*wallSpacingScale)*2-GAME_HEIGHT*headerSpacingScale), world, true);
        wallLeft.setTexture("separator-left.png");
        wallRight = new Box(new Vector2(GAME_WIDTH-GAME_WIDTH*wallSpacingScale-GAME_WIDTH*wallSizeScale, GAME_HEIGHT*wallSpacingScale), new Vector2(GAME_WIDTH*wallSizeScale, GAME_HEIGHT-(GAME_HEIGHT*wallSpacingScale)*2-GAME_HEIGHT*headerSpacingScale), world, true);
        wallRight.setTexture("separator-right.png");
        ceiling = new Box(new Vector2(GAME_WIDTH*wallSpacingScale, GAME_HEIGHT-GAME_HEIGHT*wallSpacingScale-GAME_HEIGHT*wallSizeScale-GAME_HEIGHT*headerSpacingScale), new Vector2(GAME_WIDTH-(GAME_WIDTH*wallSpacingScale)*2, GAME_HEIGHT*wallSizeScale), world, true);
        ceiling.setTexture("separator-top.png");

        walls = new ArrayList<Box>();
        walls.add(ground);
        walls.add(wallLeft);
        walls.add(wallRight);
        walls.add(ceiling);
        for (Box wall : walls)
            wall.setWall(true);
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (Box wall : walls)
            wall.render(shapeRenderer);
    }
}
