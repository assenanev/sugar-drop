package com.xquadro.sugardrop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.xquadro.sugardrop.SugarDropGame;

public abstract class AbstractScreen implements Screen {
	SugarDropGame game;

	int bgWigth, bgHeight;

	Stage stage;
	
	OrthographicCamera camera;
	SpriteBatch batch;
	public static Texture bg;
	public static TextureRegion bgRegion;

	public AbstractScreen(SugarDropGame sugarDropGame) {
		this.game = sugarDropGame;

		if(game.width >  game.height) {
			bgWigth = 1024;
			bgHeight = (int) (bgWigth / game.aspect);
		} else {
			bgHeight = 1024;
			bgWigth = (int) (bgHeight * game.aspect);
		}
		
		bg = game.assetManager.get("bg.png", Texture.class);
		bg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bgRegion = new TextureRegion(bg, (1024-bgWigth)/2, 0, bgWigth, bgHeight);

		camera = new OrthographicCamera(game.width, game.height);
		camera.position.set(game.width / 2, game.height / 2, 0);

		batch = new SpriteBatch();		
		stage = new Stage(new StretchViewport(game.width, game.height));
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage,
			new InputAdapter() {
				@Override
				public boolean keyUp(int keycode) {
					if (keycode == Keys.BACK){
						//SoundUtils.playSound(game.assetManager, "click.ogg");
						goToPrevScreen();
					}
					return true;
				}
			}));
	}
	
	abstract void goToPrevScreen();

	@Override
	public void render(float delta) {
		//TODO limit delta
        stage.act(delta);
		GL20 gl = Gdx.gl;
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.disableBlending();
		batch.begin();
		batch.draw(bgRegion, 0, 0, game.width, game.height);
		batch.end();
		batch.enableBlending();
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();		
		batch.dispose();
	}

}
