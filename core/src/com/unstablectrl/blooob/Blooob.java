package com.unstablectrl.blooob;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unstablectrl.blooob.screens.GameScreen;
import com.unstablectrl.blooob.screens.MainMenuScreen;

public class Blooob extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font/scribble.fnt"));
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}
