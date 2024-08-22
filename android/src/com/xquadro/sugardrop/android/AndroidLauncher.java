package com.xquadro.sugardrop.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.xquadro.sugardrop.Challenge.ChallengeType;
import com.xquadro.sugardrop.SugarDropGame;
import com.xquadro.sugardrop.controllers.IAdsController;
import com.xquadro.sugardrop.controllers.IGPGSController;

public class AndroidLauncher extends AndroidApplication implements 
	IGPGSController, IAdsController {
	private static final String BANNER_AD_UNIT_ID = "123";
	private static final String INTERSTITIAL_AD_UNIT_ID = "123";
	private static final boolean ENABLE_INTERSTITIAL_ADS = false;
	private static final boolean ENABLE_BANNER_ADS = false;
	
	private GameHelper gameHelper;

	private AdView bannerView;
	private InterstitialAd interstitialAd;
	private View gameView;
	private long currentScore;
	private String currentLeaderboard;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		
		View gameView = createGameView(config);
		layout.addView(gameView);
		
		AdView admobView = createBannerView();
		layout.addView(admobView);
		
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
		
		setContentView(layout);
		
		if(ENABLE_BANNER_ADS) {
			startBannerAds(admobView);
			showBannerAd(true);
		}
		
		prepareInterstitialAd();
		
		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.setMaxAutoSignInAttempts(0);
			gameHelper.enableDebugLog(true);
		}
		
		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()	{
			@Override
			public void onSignInSucceeded() {
			}

			@Override
			public void onSignInFailed(){
			}
		};
		gameHelper.setup(gameHelperListener);
	}
	
	private AdView createBannerView() {
		bannerView = new AdView(this);
		bannerView.setAdSize(AdSize.SMART_BANNER);
		bannerView.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerView.setId(1); // this is an arbitrary id, allows for relative positioning in createGameView()
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);		
		bannerView.setLayoutParams(adParams);
		bannerView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		return bannerView;
	}
	

	
	
	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new SugarDropGame(this, this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		//params.addRule(RelativeLayout.BELOW, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}
	
	private void startBannerAds(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice("23C074AE7DD5818701D54BFB29D3B9F8") //2nd nexus 
			.build();
		adView.loadAd(adRequest);
	}

	@Override
	public void showBannerAd(boolean show) {
		if(show) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bannerView.setVisibility(View.VISIBLE);
				}
			});
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bannerView.setVisibility(View.GONE);
				}
			});
		}
		
	}
	
	@Override
	public void prepareInterstitialAd() {
		runOnUiThread(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				if(ENABLE_INTERSTITIAL_ADS && !interstitialAd.isLoaded()) {
					AdRequest ad = new AdRequest.Builder().addTestDevice(
							"23C074AE7DD5818701D54BFB29D3B9F8") // 2nd nexus
							.build();
					interstitialAd.loadAd(ad);
				}	
			}
		});
	}
	
	@Override
	public void showInterstitialAd(final Runnable then) {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(interstitialAd.isLoaded()){
						if (then != null) {
							interstitialAd.setAdListener(new AdListener() {
								@Override
								public void onAdClosed() {
									Gdx.app.postRunnable(then);
								}
							});
						}
						interstitialAd.show();
					} else {
						prepareInterstitialAd();
						if (then != null) {
							Gdx.app.postRunnable(then);
						}
					}
				}
			}); 
		} catch (Exception e) {
			
		}

	}

	@Override
	public void signInGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}

	}

	@Override
	public boolean isSignedInGPGS() {
		return gameHelper.isSignedIn();
	}
	
	@Override
	public void submitScoreGPGS(int score, ChallengeType challengeType) {
		String leaderboardId;
		
		switch (challengeType) {
		case HYPERACTIVE:
			leaderboardId = getString(R.string.leaderboard_hyperactive_high_scores);
			break;
		case SUGAR_RUSH:
			leaderboardId = getString(R.string.leaderboard_sugar_rush_high_scores);
			break;
		case SUGARDRIVE:
			leaderboardId = getString(R.string.leaderboard_sugardrive_high_scores);
			break;
		case QUICK_PLAY:
			leaderboardId = getString(R.string.leaderboard_quick_play_high_scores);
			break;
		default:
			leaderboardId = getString(R.string.leaderboard_quick_play_high_scores);
			break;
		}
		if (gameHelper.isSignedIn()) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardId, score);
			currentScore = score;
			currentLeaderboard = leaderboardId;
			Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
					gameHelper.getApiClient(), leaderboardId,
					LeaderboardVariant.TIME_SPAN_ALL_TIME,
					LeaderboardVariant.COLLECTION_SOCIAL).setResultCallback(
					new ResultCallback<LoadPlayerScoreResult>() {

						@Override
						public void onResult(LoadPlayerScoreResult scoreResult) {
							if (isScoreResultValid(scoreResult)) {
								long leaderboardScore = scoreResult.getScore()
										.getRawScore();
								if (currentScore <= leaderboardScore) {
									startActivityForResult(
											Games.Leaderboards.getLeaderboardIntent(
													gameHelper.getApiClient(), currentLeaderboard),
											100);
								}
							}
						}
					});
		} 
	}
	
	private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {
	    return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
	}


	@Override
	public void getLeaderboardGPGS(String leaderboardId) {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							gameHelper.getApiClient(), leaderboardId),
					100);
		} else if (!gameHelper.isConnecting()) {
			signInGPGS();
		}

	}
	
	@Override
	public void getLeaderboardsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient()), 102);
		} else if (!gameHelper.isConnecting()) {
			signInGPGS();
		}

	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);		
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		} else if (!gameHelper.isConnecting()) {
			signInGPGS();
		}
		
	}
	
	@Override
	public void rateGameGPGS() {
		try{
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));	
		} catch (Exception e) {
			
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);		
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onDestroy() {
		if (bannerView != null) {
			bannerView.destroy();
		}
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		if (bannerView != null) {
			bannerView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (bannerView != null) {
			bannerView.resume();
		}
	}
	
	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

}
