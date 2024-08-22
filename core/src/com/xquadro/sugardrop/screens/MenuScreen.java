package com.xquadro.sugardrop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xquadro.sugardrop.Settings;
import com.xquadro.sugardrop.SoundUtils;
import com.xquadro.sugardrop.SugarDropGame;

public class MenuScreen extends AbstractScreen {

	public static TextureAtlas atlas;

	boolean showSettings = false;

	ImageButton btnSound;

	ImageButton btnMusic;
	
	final int btnSize = 110;
    int btnSettingsPos = 40;
    float btnSettingsScale = 0.75f;
    float btnSoundScale = 0.6f;
    int btnSountOffset = (int) (btnSize * (btnSettingsScale - btnSoundScale)/2);
    
    Color colorBtn = new  Color(1, 1, 1, 0.75f);
    Color colorBtn1 = new  Color(1, 1, 1, 0f);

	public MenuScreen(SugarDropGame sugarDropGame) {
		super(sugarDropGame);
		
		atlas = game.assetManager.get("atlases/cc.atlas", TextureAtlas.class);
		
		Image splash = new Image(atlas.findRegion("splash"));
		
		splash.setSize(SugarDropGame.VIRTUAL_WIDTH, SugarDropGame.VIRTUAL_WIDTH);
		
		splash.setPosition((game.width- splash.getWidth())/2, 
				game.height - splash.getHeight() + 50);
		
		stage.addActor(splash);

		
		
		ImageButton btnPlay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnPlay")),
        		new TextureRegionDrawable(atlas.findRegion("btnPlayDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnPlay")));
		btnPlay.setSize(btnSize, btnSize);
		btnPlay.setColor(colorBtn);
		btnPlay.setPosition(285, 295);
		
        btnPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(game, 0));
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});
        stage.addActor(btnPlay);
        
        ImageButton btnChallenge = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnChallenge")),
        		new TextureRegionDrawable(atlas.findRegion("btnChallengeDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnChallenge")));
        btnChallenge.setSize(btnSize * 1.25f, btnSize * 1.25f);
		btnChallenge.setColor(colorBtn);
        btnChallenge.setPosition(95, 315);
		
        btnChallenge.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new ChallengeScreen(game));
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}
		        
		});
        stage.addActor(btnChallenge);
        
        ImageButton btnScore = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnScore")),
        		new TextureRegionDrawable(atlas.findRegion("btnScoreDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnScore")));
        btnScore.setSize(btnSize * btnSettingsScale, btnSize * btnSettingsScale);
        btnScore.setColor(colorBtn);
        btnScore.setPosition(265, 158);
		
        btnScore.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundUtils.playSound(game.assetManager, "click.ogg");
				game.getGpgsController().getLeaderboardsGPGS();
			}
		        
		});
        stage.addActor(btnScore);
        
        btnSound = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnSoundOff")),
        		null, new TextureRegionDrawable(atlas.findRegion("btnSound")));
        btnSound.setPosition(btnSettingsPos + btnSountOffset, btnSettingsPos + btnSountOffset);
        btnSound.setSize(btnSize * btnSoundScale, btnSize * btnSoundScale);
        btnSound.setChecked(Settings.soundEnabled);
        btnSound.setColor(colorBtn1);
        btnSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Settings.soundEnabled = btnSound.isChecked();		
				Settings.save();	
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnSound);
        
        btnMusic = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnMusicOff")),
        		null, new TextureRegionDrawable(atlas.findRegion("btnMusic")));
        btnMusic.setPosition(btnSettingsPos + btnSountOffset, btnSettingsPos + btnSountOffset);
        btnMusic.setSize(btnSize * btnSoundScale, btnSize * btnSoundScale);
        btnMusic.setChecked(Settings.musicEnabled);
        btnMusic.setColor(colorBtn1);
        btnMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Settings.musicEnabled = btnMusic.isChecked();	
				Settings.save();
				SoundUtils.toggleMusic(game.assetManager);
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnMusic);
        
        ImageButton btnSettings = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnSettings")),
        		new TextureRegionDrawable(atlas.findRegion("btnSettingsDown")), 
        		new TextureRegionDrawable(atlas.findRegion("btnSettings")));
        
		btnSettings.setPosition(btnSettingsPos, btnSettingsPos);
		btnSettings.setSize(btnSize * btnSettingsScale, btnSize * btnSettingsScale);
		btnSettings.setColor(colorBtn);
		btnSettings.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showSettings = !showSettings;
				if(showSettings){
					btnSound.addAction(Actions.moveTo(btnSettingsPos + btnSountOffset + 80, btnSettingsPos + btnSountOffset+20, 0.3f));	
					btnMusic.addAction(Actions.moveTo(btnSettingsPos + btnSountOffset + 20, btnSettingsPos + btnSountOffset + 80, 0.3f));
					btnSound.addAction(Actions.alpha(0.75f, 0.3f));
					btnMusic.addAction(Actions.alpha(0.75f, 0.3f));
				} else {
					btnSound.addAction(Actions.moveTo(btnSettingsPos + btnSountOffset, btnSettingsPos + btnSountOffset, 0.3f));	
					btnMusic.addAction(Actions.moveTo(btnSettingsPos + btnSountOffset, btnSettingsPos + btnSountOffset, 0.3f));
					btnSound.addAction(Actions.alpha(0f, 0.3f));
					btnMusic.addAction(Actions.alpha(0f, 0.3f));
				}
				SoundUtils.playSound(game.assetManager, "click.ogg");
			}		        
		});
        stage.addActor(btnSettings);
	}

	@Override
	void goToPrevScreen() {
		Gdx.app.exit();		
	}
}
