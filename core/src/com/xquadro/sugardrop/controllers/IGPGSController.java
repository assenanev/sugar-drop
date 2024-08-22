package com.xquadro.sugardrop.controllers;

import com.xquadro.sugardrop.Challenge.ChallengeType;

public interface IGPGSController {
	public void signInGPGS();
	public boolean isSignedInGPGS();
	public void submitScoreGPGS(int score, ChallengeType challengeType);
	public void getLeaderboardGPGS(String leaderboardId);
	public void getLeaderboardsGPGS();
	public void unlockAchievementGPGS(String achievementId);
	public void getAchievementsGPGS();
	public void rateGameGPGS();
	
}
