package com.xquadro.sugardrop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.xquadro.sugardrop.Challenge.ChallengeType;
import com.xquadro.sugardrop.SugarDropGame;
import com.xquadro.sugardrop.controllers.IAdsController;
import com.xquadro.sugardrop.controllers.IGPGSController;

public class DesktopLauncher implements IGPGSController, IAdsController {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		
		
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		settings.filterMin = TextureFilter.Linear;
		settings.filterMag = TextureFilter.Linear;
		settings.paddingX = 2;
		settings.paddingY = 2;
		//settings.duplicatePadding = true;
		settings.flattenPaths = true;
		TexturePacker.process(settings, "images", "../android/assets/atlases", "cc");
		
		DesktopLauncher application = new DesktopLauncher();
		new LwjglApplication(new SugarDropGame(application, application), config);
	}

	@Override
	public void signInGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSignedInGPGS() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void unlockAchievementGPGS(String achievementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitScoreGPGS(int score, ChallengeType challengeType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardsGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showBannerAd(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showInterstitialAd(Runnable then) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareInterstitialAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rateGameGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS(String leaderboardId) {
		// TODO Auto-generated method stub
		
	}
}