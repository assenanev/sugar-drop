package com.xquadro.sugardrop.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.xquadro.sugardrop.Candy;
import com.xquadro.sugardrop.world.GameWorld.State;

public class GameWorldRenderer {
	static final float HEIGHT = 20;
	//static final float HEIGHT = 50;

	GameWorld world;
	SpriteBatch batch;
	OrthographicCamera camera;

	AssetManager assetManager;

	TextureRegion background;
	TextureAtlas atlas;
	AtlasRegion bg;

	private Sprite jarSprite;
	Box2DDebugRenderer debugRenderer;

	public GameWorldRenderer(SpriteBatch batch, GameWorld world,
			AssetManager assetManager, float aspect) {
		this.world = world;
		this.assetManager = assetManager;
		this.batch = batch;
		this.atlas = assetManager.get("atlases/cc.atlas", TextureAtlas.class);

		camera = new OrthographicCamera(HEIGHT * aspect, HEIGHT);
		camera.position.set(0, 10, 0);
		//camera.position.set(0, 25, 0);
		
		createSprites();
		
		debugRenderer = new Box2DDebugRenderer();
	}

	private void createSprites() {
		Texture jarTexture = assetManager.get("jar.png", Texture.class);
		jarTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion jarRegion = new TextureRegion(jarTexture, 0, 0, 1024,
				1024);

		jarSprite = new Sprite(jarRegion);
		jarSprite.setSize(GameWorld.JAR_HEIGHT, GameWorld.JAR_HEIGHT);
		jarSprite.setPosition(-10, 0);
	}

	public void render() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.begin();
		jarSprite.draw(batch);
		
		if (world.state != State.ENDED) {
			renderCandy();
		}
		batch.end();

		//debugRenderer.render(world.box2dWorld, camera.combined);
	}

	private void renderCandy() {

		int len = world.activeCandy.size;

		for (int i = 0; i < len; i++) {
			Body candyModel = world.activeCandy.get(i);

			Vector2 pos = candyModel.getPosition();
			Float angle = candyModel.getAngle()* MathUtils.radiansToDegrees;
			Candy c = (Candy) candyModel.getUserData();
			TextureRegion tr = atlas.findRegion(c.getTextureName());
			Float size = world.challenge.getCandySize();
			batch.draw(tr, pos.x-size/2, pos.y-size/2, size/2, size/2, size, size, 1, 1, angle);
		}

	}
}
