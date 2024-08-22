package com.xquadro.sugardrop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;
import com.xquadro.sugardrop.Challenge.ChallengeType;
import com.xquadro.sugardrop.controllers.IAdsController;
import com.xquadro.sugardrop.controllers.IGPGSController;
import com.xquadro.sugardrop.screens.LoadScreen;

public class SugarDropGame extends Game {
	public static final int VIRTUAL_WIDTH = 480;
	public static final int VIRTUAL_HEIGHT = 800;

	private IGPGSController gspsController;
	private IAdsController adsController;

	public static final float VIRTUAL_ASPECT = (float) VIRTUAL_WIDTH
			/ VIRTUAL_HEIGHT;

	public int width, height, bgWigth, bgHeight;
	public float aspect;
	public AssetManager assetManager;

	public BitmapFont font55;
	public BitmapFont font60;
	public BitmapFont font100;
	float font55Height = 55;
	float font60Height = 60;
	float font100Height = 100;
	float fontScale = 1;

	public Array<Challenge> challenges = new Array<Challenge>();

	public SugarDropGame(IGPGSController gspsController, IAdsController adsController) {
		super();
		this.gspsController = gspsController;
		this.adsController = adsController;
	}
	
	public IGPGSController getGpgsController () {
		return gspsController;
	}
	public IAdsController getAdsController () {
		return adsController;
	}

	@Override
	public void create() {
		calculateAspect();

		assetManager = new AssetManager();
		Gdx.input.setCatchBackKey(true);
		Settings.load();
		setScreen(new LoadScreen(this));
	}

	@Override
	public void resume() {
		super.resume();
		calculateAspect();
	}

	@Override
	public void dispose() {
		super.dispose();
		Settings.incrementSessions();
		getScreen().dispose();
		assetManager.clear();
		assetManager.dispose();
	}
	
	public boolean  rateGame(){
		return false;
//		if(Settings.showRateGame){
//			Settings.showRateGame = false;
//			Settings.save();
//			gspsController.rateGameGPGS();
//			return true;
//		} else {
//			return false;
//		}
	}

	public void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/gooddc.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) Math.round(font55Height * fontScale);
		font55 = generator.generateFont(parameter); // font size 28 pixels
		font55.setScale(1 / fontScale);
		font55.setUseIntegerPositions(false);
		font55.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		parameter.size = (int) Math.round(font60Height * fontScale);
		font60 = generator.generateFont(parameter);
		font60.setScale(1 / fontScale);
		font60.setUseIntegerPositions(false);
		font60.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		parameter.size = (int) Math.round(font100Height * fontScale);
		font100 = generator.generateFont(parameter);
		font100.setScale(1 / fontScale);
		font100.setUseIntegerPositions(false);
		font100.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		calculateAspect();
	}

	public void calculateAspect() {
		aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		if (aspect < VIRTUAL_ASPECT) {
			width = VIRTUAL_WIDTH;
			height = (int) (VIRTUAL_WIDTH / aspect);
			fontScale = (float) Gdx.graphics.getWidth() / width;
		} else {
			width = (int) (VIRTUAL_HEIGHT * aspect);
			height = VIRTUAL_HEIGHT;
			fontScale = (float) Gdx.graphics.getHeight() / height;
		}
	}

	public void createLevels() {
		Challenge challenge;
		Candy candy;

		// challenge 1
		challenge = new Challenge();
		challenge.setChallengeType(ChallengeType.QUICK_PLAY)
				.setName("Quick Play").setRounds(1).setColors(3)
				.setCandySize(0.75f).setCandyCount(300);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanRed");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("jelly").setTextureName("jellyPink");
		challenge.addCandy(candy);

		challenges.add(challenge);

		// challenge 2
		challenge = new Challenge();
		challenge.setChallengeType(ChallengeType.SUGAR_RUSH)
				.setName("Sugar Rush").setRounds(5).setColors(3)
				.setCandySize(1f).setCandyCount(150);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanRed");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("jelly").setTextureName("jellyPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("lollypop2").setTextureName("lollypop2Pink");
		challenge.addCandy(candy);

		challenges.add(challenge);

		// challenge 3
		challenge = new Challenge();
		challenge.setChallengeType(ChallengeType.SUGARDRIVE)
				.setName("Sugardrive").setRounds(9).setColors(4)
				.setCandySize(1f).setCandyCount(200);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanRed");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("jelly").setTextureName("jellyPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("lollypop2").setTextureName("lollypop2Pink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("macaron").setTextureName("macaronPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballGreen");
		challenge.addCandy(candy);

		challenges.add(challenge);

		// challenge 4
		challenge = new Challenge();
		challenge.setChallengeType(ChallengeType.HYPERACTIVE)
				.setName("Hyperactive").setRounds(16).setColors(5)
				.setCandySize(1f).setCandyCount(300);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanRed");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("jelly").setTextureName("jellyPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("bean").setTextureName("beanOrange");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("lollypop2").setTextureName("lollypop2Pink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("macaron").setTextureName("macaronPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("ball").setTextureName("ballGreen");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("doughnut").setTextureName("doughnutPink");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("cake3").setTextureName("cake3Multy");
		challenge.addCandy(candy);
		candy = new Candy();
		candy.setModelName("pancakes").setTextureName("pancakesMulty");
		challenge.addCandy(candy);

		challenges.add(challenge);

		/*
		 * candy.setModelName("bean").setTextureName("beanGreen");
		 * candy.setModelName("bean").setTextureName("beanOrange");
		 * candy.setModelName("bean").setTextureName("beanRed");
		 * candy.setModelName("ball").setTextureName("ballGreen");
		 * candy.setModelName("ball").setTextureName("ballOrange");
		 * candy.setModelName("cake").setTextureName("cakeMulty");
		 * candy.setModelName("cake3").setTextureName("cake3Multy");
		 * candy.setModelName("doughnut").setTextureName("doughnutPink");
		 * candy.setModelName("jelly").setTextureName("jellyGreen");
		 * candy.setModelName("jelly").setTextureName("jellyPink");
		 * candy.setModelName("jelly").setTextureName("jellyYellow");
		 * candy.setModelName("lollypop1").setTextureName("lollypop1Pink");
		 * candy.setModelName("lollypop2").setTextureName("lollypop2Pink");
		 * candy.setModelName("lollypop3").setTextureName("lollypop3Multy");
		 * candy.setModelName("macaron").setTextureName("macaronGreen");
		 * candy.setModelName("macaron").setTextureName("macaronPink");
		 * candy.setModelName("pancakes").setTextureName("pancakesMulty");
		 * candy.setModelName("violet").setTextureName("violet");
		 */
	}
}
