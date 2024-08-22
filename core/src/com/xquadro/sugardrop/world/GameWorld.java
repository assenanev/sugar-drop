
package com.xquadro.sugardrop.world;

import java.util.Random;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.xquadro.sugardrop.Candy;
import com.xquadro.sugardrop.Challenge;

public class GameWorld {
	public enum State {INTRO, RUNNING, WAITING, ENDED};
	
	
	public interface WorldListener {

		public void win ();
	
	}

	public static final float JAR_HEIGHT = 20f;	
	public static final float INTRO_WAIT = 1f;
	public static final float COUNT_WAIT = 5f;

	private float accumulator = 0;
	private final static float TICK = 1 / 60f;
	
	public State state;
	public float stateTime;

	//models
	
	BodyEditorLoader loader;
	
	public static final Vector2 gravity = new Vector2(0, -10);
	
	public World box2dWorld;
	
	Body jarModel = null;
	Body pigModel = null;
	Body candyModel = null;
	public Array<Body> activeCandy;

	float x, y;
	
	public final WorldListener listener;
	Random rand = new Random();	
	Challenge challenge;

	public GameWorld (WorldListener listener, Challenge challenge) {
		this.listener = listener;
		this.challenge = challenge;
		activeCandy = new Array<Body>();		
		
		box2dWorld = new World(gravity, true);
		loader = new BodyEditorLoader(Gdx.files.internal("models/sugardrop.json"));
		createJar();
		state = State.INTRO;
		stateTime = 0;
	}


	private void createJar() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		FixtureDef fd = new FixtureDef();
		fd.density = 400;
		fd.friction = 0.5f;
		fd.restitution = 0f;
		jarModel = box2dWorld.createBody(bd);
		loader.attachFixture(jarModel, "jar", fd, JAR_HEIGHT);
		jarModel.setTransform(-10f, 0f, 0f);
		jarModel.setLinearVelocity(0, 0);
		jarModel.setAngularVelocity(0);
	}
	
	private void createCandy(){
		int types = challenge.getCandies().size;
		int total1 = challenge.getCandyCount();
		int total = rand.nextInt((total1 - total1/8) + 1) + total1/8;
		x = -6;
		y = 25;
		
		for (int i = 0; i < total; i++){
			int candyTypeIndex = rand.nextInt(types);
			Candy candy = challenge.getCandies().get(candyTypeIndex);
			createSingleCandy(candy);
		}
	}
	
	private void createSingleCandy(Candy candy) {
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		
		FixtureDef fd = new FixtureDef();
		fd.density = 50;
		fd.friction = 0.9f;
		fd.restitution = 0f;

		float offset = 0.5f;

		candyModel = box2dWorld.createBody(bd);
		loader.attachFixture(candyModel, candy.getModelName(), fd, challenge.getCandySize());
		candyModel.setTransform(x, y, 0f);
		candyModel.setUserData(candy);
		x += (challenge.getCandySize() + offset);
		if (x > 6) {
			x = -6;
			y += (challenge.getCandySize()+ offset);	
		}

		candyModel.setLinearVelocity(0, 0);
		candyModel.setAngularVelocity(0);
		
		candyModel.setAwake(true);
		candyModel.setActive(true);
		activeCandy.add(candyModel);		
	}

	public void prepare() {
		deleteCandy();
		createCandy();		
		state = State.INTRO;
		stateTime = 0;
	}

	private void deleteCandy() {
		for (Body b:activeCandy){
			box2dWorld.destroyBody(b);	
		}
		activeCandy.clear();
	}

	public void updatePhysics (float deltaTime) {
	      float dt = MathUtils.clamp(deltaTime, 0, 0.030f);
	      accumulator += dt;
	 
	      while(accumulator > TICK) {
	         accumulator -= TICK;
	         box2dWorld.step(TICK, 10, 10);
	      }
	   }
	
	public void update (float deltaTime) {		
		switch (state) {
		case INTRO:
			updateWating(deltaTime);
			break;
		case RUNNING:
			updateRunning(deltaTime);
			break;
		case WAITING:
			updateEnding(deltaTime);
			break;
		case ENDED:			
			break;
		default:
			break;
		}
		
	}
	
	private void updateWating(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime > INTRO_WAIT) {
			state = State.RUNNING;
			stateTime = 0;
		}
	}
	
	private void updateEnding(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime > COUNT_WAIT) {
			state = State.ENDED;
			stateTime = 0;
		}
	}

	public void updateRunning (float deltaTime) {
		stateTime += deltaTime;
		updatePhysics(deltaTime);
		boolean alive = false;
		for (int i = 0; i < activeCandy.size; i++){
			Body candyM = activeCandy.get(i);
			if (candyM.getPosition().y < -5){
				activeCandy.removeValue(candyM, true);
				box2dWorld.destroyBody(candyM);				
			}
			
			if(candyM.getPosition().y > 0f && candyM.isAwake()){
				alive = true;
			}
			
			
		}
		if(!alive || stateTime > 8f) {
			state = State.WAITING;
			stateTime = 0;
		}

	}
	
	public void dispose () {
		activeCandy.clear();
		box2dWorld.dispose();
	}


}
