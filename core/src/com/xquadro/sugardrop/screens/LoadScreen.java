package com.xquadro.sugardrop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xquadro.sugardrop.Settings;
import com.xquadro.sugardrop.SoundUtils;
import com.xquadro.sugardrop.SugarDropGame;

public class LoadScreen implements Screen {
	
	private SugarDropGame game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private static Texture splashTexture;
	public static Texture bg;
	public static TextureRegion bgRegion;

	private float splashAspect;
	private int splashWidth, splashHeight;
	private float activeTime;
	//private float minActiveTime = 3f;
	private float minActiveTime = 0f;
	AssetManager manager;
	
	public LoadScreen(SugarDropGame game) {
		super();
		this.game = game;

		manager = game.assetManager;
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.BACK){
					Gdx.app.exit();
				}
				return true;
			}
		});
		
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		param.magFilter = TextureFilter.Linear;

		manager.load("bg.png", Texture.class, param);
		manager.load("jar.png", Texture.class, param);
		manager.load("atlases/cc.atlas", TextureAtlas.class);


		manager.load("sounds/click.ogg", Sound.class);

		manager.load("music/funny_loop.ogg", Music.class);

		Texture.setAssetManager(manager);

		batch = new SpriteBatch();
		camera = new OrthographicCamera(game.width, game.height);
		camera.position.set(game.width / 2, game.height / 2, 0);
		
		splashTexture = new Texture(Gdx.files.internal("splash.png"));
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		splashAspect = (float) splashTexture.getWidth()/splashTexture.getHeight();
		splashWidth = (int) (game.width * 0.9f);
		splashHeight = (int) (splashWidth / splashAspect);
		activeTime = 0;
	}
	
	@Override
	public void render(float delta) {
		activeTime += delta;
		
		if(manager.update() && activeTime > minActiveTime){
			game.loadFonts();
			game.createLevels();
			SoundUtils.playMusic(manager);
			//game.gspsHandler.isSignedInGPGS();	
			if(Settings.gameSessions < 1) {
				game.getGpgsController().signInGPGS();
			}
			game.setScreen(new MenuScreen(game));
		}
		
		GL20 gl = Gdx.gl;

		gl.glClearColor(0f, 0.89f, 0.99f, 1f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(splashTexture, game.width/2 - splashWidth/2, game.height/2 - splashHeight/2, splashWidth, splashHeight);
		batch.end();
		
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
		// TODO Auto-generated method stub
		
	}

}
