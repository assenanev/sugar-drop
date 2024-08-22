package com.xquadro.sugardrop.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xquadro.sugardrop.Challenge;
import com.xquadro.sugardrop.SoundUtils;
import com.xquadro.sugardrop.SugarDropGame;

public class ChallengeScreen extends AbstractScreen {

	public static TextureAtlas atlas;

	Color colorBtn = new  Color(1, 1, 1, 0.75f);

	public ChallengeScreen(SugarDropGame sugarDropGame) {
		super(sugarDropGame);
		
		atlas = game.assetManager.get("atlases/cc.atlas", TextureAtlas.class);
		
		Table scrollTable = new Table();
		
		float dialogSize = 400;
		
		for (int i = 0; i < game.challenges.size; i++){
			
			Challenge challenge = game.challenges.get(i);			
			Group gBisquit = new Group();
			gBisquit.setSize(dialogSize, dialogSize * 0.67f);
			
			Image biscuit = new Image(atlas.findRegion("biscuit"));
			biscuit.setSize(gBisquit.getWidth(), gBisquit.getHeight());
			biscuit.setPosition(0, 0);
			gBisquit.addActor(biscuit);
			
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = game.font60;
			labelStyle.fontColor = Color.BLACK;
			
			LabelStyle labelStyle1 = new LabelStyle();
			labelStyle1.font = game.font60;
			labelStyle1.fontColor = Color.RED;
		
			Label challengeName = new Label(challenge.getName(), labelStyle);
			challengeName.setAlignment(Align.center);
			challengeName.setPosition(gBisquit.getWidth()/2 - challengeName.getWidth()/2, 170);
			gBisquit.addActor(challengeName);
			Label challengeName2 = new Label(challenge.getName(), labelStyle1);
			challengeName2.setAlignment(Align.center);
			challengeName2.setPosition(challengeName.getX()-2, challengeName.getY()+2);
			gBisquit.addActor(challengeName2);
			
			LabelStyle labelStyle2 = new LabelStyle();
			labelStyle2.font = game.font55;
			labelStyle2.fontColor = Color.BLACK;
			
			Image candyCount = new Image(atlas.findRegion("candyCount"));
			candyCount.setPosition(100, 138);
			candyCount.setSize(30, 30);
			gBisquit.addActor(candyCount);
			
			Label lblCandyCount = new Label(""+(challenge.getCandies().size), labelStyle2);
			lblCandyCount.setPosition(150, 130);
			gBisquit.addActor(lblCandyCount);
			
			Image colorsCount = new Image(atlas.findRegion("colorsCount"));
			colorsCount.setPosition(100, 98);
			colorsCount.setSize(30, 30);
			gBisquit.addActor(colorsCount);
			
			Label lblColorsCount = new Label(""+challenge.getColors(), labelStyle2);
			lblColorsCount.setPosition(150, 90);
			gBisquit.addActor(lblColorsCount);
			
			Image roundsCount = new Image(atlas.findRegion("roundsCount"));
			roundsCount.setPosition(100, 58);
			roundsCount.setSize(30, 30);
			gBisquit.addActor(roundsCount);
			
			Label lblRoundsCount = new Label(""+challenge.getRounds(), labelStyle2);
			lblRoundsCount.setPosition(150, 50);
			gBisquit.addActor(lblRoundsCount);
			
			ImageButton btnPlay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("btnPlay75")),
	        		new TextureRegionDrawable(atlas.findRegion("btnPlay75Down")), 
	        		new TextureRegionDrawable(atlas.findRegion("btnPlay75")));
			btnPlay.setSize(55, 55);
			btnPlay.setPosition(gBisquit.getWidth() - btnPlay.getWidth()*2, btnPlay.getHeight());
			btnPlay.setName(""+i);
			btnPlay.setColor(colorBtn);
	        btnPlay.addListener(new ChangeListener() {
	
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					int challengeIndex = Integer.parseInt(actor.getName());
					game.setScreen(new GameScreen(game, challengeIndex));
					SoundUtils.playSound(game.assetManager, "click.ogg");
				}
			        
			});
	        
	        gBisquit.addActor(btnPlay);
	
			scrollTable.add().height(50);
			scrollTable.row();
			scrollTable.add(gBisquit);//.width(400).height(269);
			scrollTable.row();
		}
		
		scrollTable.add().height(50);
		scrollTable.row();
//
		ScrollPaneStyle style = new ScrollPaneStyle(null, null, null, null, null);
		ScrollPane pane = new ScrollPane(scrollTable, style);
		pane.setScrollingDisabled(true, false);
		pane.setWidth(400);
		pane.setHeight(game.height);//pane.pack();
		pane.setPosition((game.width - pane.getWidth())/2, 0);
		pane.validate();
		stage.addActor(pane);
	
//		scrollTable.debug();

	}

	@Override
	void goToPrevScreen() {
		game.setScreen(new MenuScreen(game));		
	}
}
