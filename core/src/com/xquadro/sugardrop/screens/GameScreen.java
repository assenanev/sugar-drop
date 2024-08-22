/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.xquadro.sugardrop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.xquadro.sugardrop.Challenge;
import com.xquadro.sugardrop.SoundUtils;
import com.xquadro.sugardrop.SugarDropGame;
import com.xquadro.sugardrop.world.GameWorld;
import com.xquadro.sugardrop.world.GameWorld.WorldListener;
import com.xquadro.sugardrop.world.GameWorldRenderer;

public class GameScreen implements Screen {
	public enum State {PREPARE, RUN, GUESS, CHECK_RESULT, FINISH};
	
	final SugarDropGame game;
	
	TextureAtlas atlas;
	SpriteBatch batch;
	OrthographicCamera guiCam;
	Stage stage;
	
	GameWorld gameWorld;
	WorldListener worldListener;
	GameWorldRenderer renderer;
	
	int bgWigth, bgHeight;
	static Texture bg;
	static TextureRegion bgRegion;

	State state;
	float stateTime;
	
	Challenge challenge;
	int challengeIndex;
	
	int currentRound;
	int score;

	private int one = 0;
	private int ten = 0;
	private int hundred = 0;	

	FPSLogger fps = new FPSLogger();
	
	Color colorBtn = new  Color(1, 1, 1, 0.75f);
	private NinePatchDrawable npd;
	private Label ones, tens, hundreds; 
	private Group guessGroup;
	private Group resultGroup;
	private Label lblResult;
	private Label lblResult2;
	private Label lblTotal;
	private Label lblTotal2;
	private Group finishGroup;
	private Label lblScoreFinal;
	private Label lblScoreFinal2;
	private Label lblScore;
	private Label lblScore2;
	private Label lblRound;
	private Label lblRound2;

	public GameScreen(SugarDropGame sugarDropGame, int challengeIndex) {
		this.game = sugarDropGame;
		

		atlas = game.assetManager.get("atlases/cc.atlas",
				TextureAtlas.class);
		
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
		
		challenge = game.challenges.get(challengeIndex);
		this.challengeIndex = challengeIndex;
		currentRound = 0;
		score = 0;
		
		state = State.PREPARE;
		stateTime = 0;

		guiCam = new OrthographicCamera(game.width, game.height);
		guiCam.position.set(game.width / 2, game.height / 2, 0);
		guiCam.update();
		batch = new SpriteBatch();
		
		stage = new Stage(new StretchViewport(game.width, game.height));
		
		
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage,
			new InputAdapter() {
				@Override
				public boolean keyUp(int keycode) {
					if (keycode == Keys.BACK){
						game.setScreen(new MenuScreen(game));
					}
					return true;
				}
				
			}));

		setupStage();

		worldListener = new WorldListener() {
			@Override
			public void win() {
			}

		};
		gameWorld = new GameWorld(worldListener, challenge);

		renderer = new GameWorldRenderer(batch, gameWorld, game.assetManager, game.aspect);
		
		//prepare ads if not loaded
		game.getAdsController().prepareInterstitialAd();
	}
	
	private void setupGuessGroup(){
		
		int btnSmallSize = 65;
		int btnSize = 100;
		int btnUpSize = 65;
		float dialogSize = 400;
			
		guessGroup = new Group();
		guessGroup.setSize(dialogSize, dialogSize * 1.42f);
		guessGroup.setPosition(game.width/2 - guessGroup.getWidth()/2, 
				game.height/2 - guessGroup.getHeight()/2);
		
		Image biscuitBig = new Image(atlas.findRegion("biscuitBig"));
		biscuitBig.setSize(guessGroup.getWidth(), guessGroup.getHeight());
		biscuitBig.setPosition(0, 0);
		guessGroup.addActor(biscuitBig);
		
		Image candyIcon = new Image(atlas.findRegion("candyIcon")); 
		candyIcon.setSize(60, 60);
		candyIcon.setPosition(110, 450);
		guessGroup.addActor(candyIcon);
		
		LabelStyle style1 = new LabelStyle();
		style1.font = game.font100;
		style1.fontColor = new Color(0f, 0f, 0f, 0.8f);
		
		Label heading = new Label("= ?", style1);
		heading.setPosition(190, 430);
		guessGroup.addActor(heading);
		
		LabelStyle style2 = new LabelStyle();
		style2.font = game.font100;
		style2.fontColor = new Color(0.34f, 0.76f, 0.75f, 1);
		Label heading2 = new Label("= ?", style2);
		heading2.setPosition(heading.getX()-4, heading.getY()+4);
		guessGroup.addActor(heading2);
		
		ImageButton btnMenu = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnMenu")),
        		new TextureRegionDrawable(atlas.findRegion("btnMenuDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnMenu")));
		btnMenu.setSize(btnSmallSize, btnSmallSize);		
		btnMenu.setPosition(btnSmallSize, btnSmallSize);
		btnMenu.setColor(colorBtn);
		btnMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));;
					}
				});
			}
		        
		});
		guessGroup.addActor(btnMenu);
		
		ImageButton btnReplay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnReplay")),
        		new TextureRegionDrawable(atlas.findRegion("btnReplayDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnReplay")));
		btnReplay.setSize(btnSmallSize, btnSmallSize);		
		btnReplay.setPosition(guessGroup.getWidth() - 2*btnSmallSize, btnSmallSize);
		btnReplay.setColor(colorBtn);
		btnReplay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new GameScreen(game, challengeIndex));
					}
				});
			}
		        
		});		
		guessGroup.addActor(btnReplay);
		
		npd = new NinePatchDrawable(atlas.createPatch("txtBg"));
		
		LabelStyle style = new LabelStyle();
		style.font = game.font55;
		style.fontColor = Color.BLACK;
		style.background = npd;
		
		float position = 360;
		float txtOffset = 15;
		
		ImageButton btnUp100 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnUp")),
        		new TextureRegionDrawable(atlas.findRegion("btnUpDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnUp")));
		btnUp100.setSize(btnUpSize, btnUpSize);	
		btnUp100.setColor(colorBtn);
		btnUp100.setPosition(guessGroup.getWidth()/2 - btnUpSize/2 - btnUpSize - txtOffset, position);
		btnUp100.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				hundred++;
				if (hundred>9) hundred = 0;
				hundreds.setText(""+hundred);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnUp100);
		
		hundreds = new Label("0", style);
		hundreds.setSize(btnUpSize, btnUpSize);
		hundreds.setPosition(guessGroup.getWidth()/2 - hundreds.getWidth()/2 - btnUpSize - txtOffset, position - btnUpSize);
		guessGroup.addActor(hundreds);
		
		ImageButton btnDown100 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnDown")),
        		new TextureRegionDrawable(atlas.findRegion("btnDownDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnDown")));
		btnDown100.setSize(btnUpSize, btnUpSize);
		btnDown100.setColor(colorBtn);
		btnDown100.setPosition(guessGroup.getWidth()/2 - btnUpSize/2 - btnUpSize - txtOffset, position - 2*btnUpSize);
		btnDown100.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				hundred--;
				if (hundred<0) hundred = 9;
				hundreds.setText(""+hundred);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnDown100);
		
		ImageButton btnUp10 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnUp")),
        		new TextureRegionDrawable(atlas.findRegion("btnUpDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnUp")));
		btnUp10.setSize(btnUpSize, btnUpSize);		
		btnUp10.setColor(colorBtn);
		btnUp10.setPosition(guessGroup.getWidth()/2 - btnUpSize/2, position);
		btnUp10.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ten++;
				if (ten>9) ten = 0;
				tens.setText(""+ten);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnUp10);
		
		tens = new Label("0", style);
		tens.setSize(btnUpSize, btnUpSize);
		tens.setPosition(guessGroup.getWidth()/2 - tens.getWidth()/2, position - btnUpSize);
		guessGroup.addActor(tens);
		
		ImageButton btnDown10 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnDown")),
        		new TextureRegionDrawable(atlas.findRegion("btnDownDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnDown")));
		btnDown10.setSize(btnUpSize, btnUpSize);	
		btnDown10.setColor(colorBtn);
		btnDown10.setPosition(guessGroup.getWidth()/2 - btnUpSize/2, position - 2*btnUpSize);
		btnDown10.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ten--;
				if (ten<0) ten = 9;
				tens.setText(""+ten);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnDown10);
		
		ImageButton btnUp1 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnUp")),
        		new TextureRegionDrawable(atlas.findRegion("btnUpDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnUp")));
		btnUp1.setSize(btnUpSize, btnUpSize);	
		btnUp1.setColor(colorBtn);
		btnUp1.setPosition(guessGroup.getWidth()/2 - btnUpSize/2 + btnUpSize + txtOffset, position);
		btnUp1.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				one++;
				if (one>9) one = 0;
				ones.setText(""+one);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnUp1);
		
		ones = new Label("0", style);
		ones.setSize(btnUpSize, btnUpSize);
		ones.setPosition(guessGroup.getWidth()/2 - btnUpSize/2 + btnUpSize + txtOffset, position - btnUpSize);
		guessGroup.addActor(ones);
		
		ImageButton btnDown1 = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnDown")),
        		new TextureRegionDrawable(atlas.findRegion("btnDownDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnDown")));
		btnDown1.setSize(btnUpSize, btnUpSize);	
		btnDown1.setColor(colorBtn);
		btnDown1.setPosition(guessGroup.getWidth()/2 - btnUpSize/2 + btnUpSize + txtOffset, position - 2*btnUpSize);
		btnDown1.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				one--;
				if (one<0) one = 9;
				ones.setText(""+one);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		guessGroup.addActor(btnDown1);
		
		ImageButton btnGuess = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnGuess")),
        		new TextureRegionDrawable(atlas.findRegion("btnGuessDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnGuess")));
		btnGuess.setSize(btnSize, btnSize);	
		btnGuess.setColor(colorBtn);
		btnGuess.setPosition(guessGroup.getWidth()/2 - btnSize/2, 120);
		btnGuess.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int myguess = one+ten*10+hundred*100;
				guessGroup.setVisible(false);
				lblResult.setText(""+myguess);
				lblResult2.setText(""+myguess);
				lblTotal.setText(""+gameWorld.activeCandy.size);
				lblTotal2.setText(""+gameWorld.activeCandy.size);				
				
				score += Math.abs(gameWorld.activeCandy.size - myguess);
				
				lblScoreFinal.setText(""+score);
				lblScoreFinal2.setText(""+score);
				lblScore.setText(""+score);
				lblScore2.setText(""+score);
				
				state = State.CHECK_RESULT;
				stateTime = 0;
				ones.setText("" + 0);
				tens.setText("" + 0);
				hundreds.setText("" + 0);;
				one = 0;
				ten = 0;
				hundred = 0;	
				resultGroup.setVisible(true);	
				SoundUtils.playSound(game.assetManager, "click.ogg");
				
			}
		        
		});		
		guessGroup.addActor(btnGuess);
		guessGroup.setVisible(false);
		stage.addActor(guessGroup);
	}

	
	public boolean checkGameOver(){
		if (currentRound == challenge.getRounds() - 1){
			state = State.FINISH;
			stateTime = 0;
			return true;					
		} 
		return false;
	}
	
	private void setupResultGroup(){
		
		int btnSmallSize = 65;
		int btnSize = 100;
		float dialogSize = 400;
			
		resultGroup = new Group();
		resultGroup.setSize(dialogSize, dialogSize * 1.42f);
		resultGroup.setPosition(game.width/2 - resultGroup.getWidth()/2, 
				game.height/2 - resultGroup.getHeight()/2);
		
		Image biscuitBig = new Image(atlas.findRegion("biscuitBig"));
		biscuitBig.setSize(resultGroup.getWidth(), resultGroup.getHeight());
		biscuitBig.setPosition(0, 0);
		resultGroup.addActor(biscuitBig);
		
		Image candyIcon = new Image(atlas.findRegion("candyIcon")); 
		candyIcon.setSize(50, 50);
		candyIcon.setPosition(60, 430);
		resultGroup.addActor(candyIcon);
		
		LabelStyle style1 = new LabelStyle();
		style1.font = game.font100;
		style1.fontColor = new Color(0f, 0f, 0f, 0.8f);
		
		lblResult = new Label("000", style1);
		lblResult.setPosition(120, 405);
		resultGroup.addActor(lblResult);
		
		Label txtVs = new Label("/", style1);
		txtVs.setPosition(resultGroup.getWidth()/2 - txtVs.getWidth()/2, 340);
		resultGroup.addActor(txtVs);
		
		lblTotal = new Label("000", style1);
		lblTotal.setPosition(180, 280);
		resultGroup.addActor(lblTotal);
		
		LabelStyle style2 = new LabelStyle();
		style2.font = game.font100;
		style2.fontColor = new Color(0.34f, 0.76f, 0.75f, 1);
		lblResult2 = new Label("000", style2);
		lblResult2.setPosition(lblResult.getX()-4, lblResult.getY()+4);
		resultGroup.addActor(lblResult2);
		
		Label txtVs2 = new Label("/", style2);
		txtVs2.setPosition(txtVs.getX()-4, txtVs.getY()+4);
		resultGroup.addActor(txtVs2);
		
		lblTotal2 = new Label("000", style2);
		lblTotal2.setPosition(lblTotal.getX()-4, lblTotal.getY()+4);
		resultGroup.addActor(lblTotal2);
		
		ImageButton btnMenu = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnMenu")),
        		new TextureRegionDrawable(atlas.findRegion("btnMenuDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnMenu")));
		btnMenu.setSize(btnSmallSize, btnSmallSize);		
		btnMenu.setPosition(btnSmallSize, btnSmallSize);
		btnMenu.setColor(colorBtn);
		btnMenu.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));;
					}
				});
			}
		        
		});
		resultGroup.addActor(btnMenu);
		
		ImageButton btnReplay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnReplay")),
        		new TextureRegionDrawable(atlas.findRegion("btnReplayDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnReplay")));
		btnReplay.setSize(btnSmallSize, btnSmallSize);		
		btnReplay.setColor(colorBtn);
		btnReplay.setPosition(resultGroup.getWidth() - 2*btnSmallSize, btnSmallSize);
		btnReplay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new GameScreen(game, challengeIndex));
					}
				});
			}
		        
		});		
		resultGroup.addActor(btnReplay);
		
		
		ImageButton btnNext = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnNext")),
        		new TextureRegionDrawable(atlas.findRegion("btnNextDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnNext")));
		btnNext.setSize(btnSize, btnSize);		
		btnNext.setColor(colorBtn);
		btnNext.setPosition(resultGroup.getWidth()/2 - btnSize/2, 160);
		btnNext.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(checkGameOver()){
					state = State.FINISH;
					stateTime = 0;
					resultGroup.setVisible(false);
					finishGroup.setVisible(true);
					game.getGpgsController().submitScoreGPGS(score, challenge.getChallengeType());
				} else {
					state = State.PREPARE;
					stateTime = 0;
					currentRound ++;
					lblRound.setText("" + (currentRound + 1) + "/"+challenge.getRounds());
					lblRound2.setText("" + (currentRound + 1) + "/"+challenge.getRounds());
					resultGroup.setVisible(false);				
				}
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});		
		resultGroup.addActor(btnNext);
		resultGroup.setVisible(false);
		stage.addActor(resultGroup);
	}

	private void setupFinishGroup(){
		
		int btnSmallSize = 65;
		float dialogSize = 400;
			
		finishGroup = new Group();
		finishGroup.setSize(dialogSize, dialogSize * 0.67f);
		finishGroup.setPosition(game.width/2 - finishGroup.getWidth()/2, 
				game.height/2 - finishGroup.getHeight()/2);
		
		Image biscuit = new Image(atlas.findRegion("biscuit"));
		biscuit.setSize(finishGroup.getWidth(), finishGroup.getHeight());
		biscuit.setPosition(0, 0);
		finishGroup.addActor(biscuit);

		LabelStyle style1 = new LabelStyle();
		style1.font = game.font100;
		style1.fontColor = new Color(0f, 0f, 0f, 0.8f);
		
		lblScoreFinal = new Label("0", style1);
		lblScoreFinal.setPosition(finishGroup.getWidth()/2 - lblScoreFinal.getWidth()/2, finishGroup.getHeight()/2);
		lblScoreFinal.setAlignment(Align.center);
		finishGroup.addActor(lblScoreFinal);
		
		LabelStyle style2 = new LabelStyle();
		style2.font = game.font100;
		style2.fontColor = new Color(0.34f, 0.76f, 0.75f, 1);
		lblScoreFinal2 = new Label(lblScoreFinal.getText(), style2);
		lblScoreFinal2.setAlignment(Align.center);
		lblScoreFinal2.setPosition(lblScoreFinal.getX()-4, lblScoreFinal.getY()+4);
		finishGroup.addActor(lblScoreFinal2);
		
		ImageButton btnMenu = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnMenu")),
        		new TextureRegionDrawable(atlas.findRegion("btnMenuDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnMenu")));
		btnMenu.setSize(btnSmallSize, btnSmallSize);	
		btnMenu.setColor(colorBtn);
		btnMenu.setPosition(btnSmallSize, btnSmallSize);
		btnMenu.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getAdsController().showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));;
					}
				});
			}
		        
		});
		finishGroup.addActor(btnMenu);
		
		ImageButton btnReplay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnReplay")),
        		new TextureRegionDrawable(atlas.findRegion("btnReplayDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnReplay")));
		btnReplay.setSize(btnSmallSize, btnSmallSize);		
		btnReplay.setPosition(resultGroup.getWidth() - 2*btnSmallSize, btnSmallSize);
		btnReplay.setColor(colorBtn);
		btnReplay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				if (!game.rateGame()){
					game.getAdsController().showInterstitialAd(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new GameScreen(game, challengeIndex));
						}
					});
				} else {
					game.setScreen(new GameScreen(game, challengeIndex));
				}
			}
		        
		});		
		finishGroup.addActor(btnReplay);
		finishGroup.setVisible(false);
		stage.addActor(finishGroup);
	}

	private void setupStage() {
		LabelStyle styleTop = new LabelStyle();
		styleTop.font = game.font60;
		styleTop.fontColor = new Color(1f, 1f, 1f, 1f);
		
		LabelStyle styleTop1 = new LabelStyle();
		styleTop1.font = game.font60;
		styleTop1.fontColor = new Color(0f, 0f, 0f, 0.8f);
		
		lblScore = new Label(""+score, styleTop); 
		lblScore.setPosition(game.width - lblScore.getWidth() - 20, 15);
		lblScore.setAlignment(Align.right);
		
		lblScore2 = new Label(""+score, styleTop1); 
		lblScore2.setPosition(lblScore.getX()+3, lblScore.getY() - 3);
		lblScore2.setAlignment(Align.right);
		stage.addActor(lblScore2);
		stage.addActor(lblScore);
		
		lblRound = new Label("" + (currentRound + 1) + "/"+challenge.getRounds(), styleTop); 
		lblRound.setPosition(20, 15);
		lblRound.setAlignment(Align.left);
		lblRound2 = new Label("" + (currentRound + 1)  + "/"+challenge.getRounds(), styleTop1);
		lblRound2.setPosition(lblRound.getX()+3, lblRound.getY() - 3);
		lblRound2.setAlignment(Align.left);
		stage.addActor(lblRound2);
		stage.addActor(lblRound);
		
		setupResultGroup();
		setupGuessGroup();
		setupFinishGroup();
	}


	
	public void update(float deltaTime) {

		stage.act(deltaTime);
		
		switch (state) {
			case PREPARE:
				updatePrepate();
			case RUN:
				updateRunning(deltaTime);
				break;
			case GUESS:
				break;
			case CHECK_RESULT:
				updateCheckResult();
				break;
			case FINISH:
				break;
		}
	}

	private void updatePrepate() {
		gameWorld.prepare();
		state = State.RUN;		
	}
	
	private void updateRunning(float deltaTime) {

		gameWorld.update(deltaTime);
		
		if(gameWorld.state == GameWorld.State.ENDED){
			state = State.GUESS;
			guessGroup.setVisible(true);
		}
	}
	
	private void updateCheckResult() {
		
		if(currentRound > challenge.getRounds()){
			
		} else {
			
		}
		
	}

	public void draw(float deltaTime) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		guiCam.update();
		batch.setProjectionMatrix(guiCam.combined);
		
		batch.disableBlending();
		batch.begin();
		batch.draw(bgRegion, 0, 0, game.width, game.height);
		batch.end();
		batch.enableBlending();
		
		renderer.render();
		stage.draw();

	}


	@Override
	public void render(float delta) {
		//fps.log();
		
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		//game.myRequestHandler.showAds(false);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {		
		stage.dispose();
		batch.dispose();		
		gameWorld.dispose();	
	}
}
