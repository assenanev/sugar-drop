package com.xquadro.sugardrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	public static final Preferences prefs = Gdx.app.getPreferences(".sugardrop");
	public static boolean soundEnabled = true;
	public static boolean musicEnabled = true;
	public static boolean showRateGame = false;
	public static int gameSessions = 0;
	
	public static void load() {
		if (!prefs.contains("soundEnabled")
				|| !prefs.contains("musicEnabled")
				|| !prefs.contains("showRateGame")
				|| !prefs.contains("gameSessions")
				) {
			save();
		}

		soundEnabled = prefs.getBoolean("soundEnabled", true);
		musicEnabled = prefs.getBoolean("musicEnabled", true);	
		showRateGame = prefs.getBoolean("showRateGame", false);	
		gameSessions = prefs.getInteger("gameSessions", 0);
	}

	public static void save() {
		prefs.putBoolean("soundEnabled", soundEnabled);
		prefs.putBoolean("musicEnabled", musicEnabled);
		prefs.putBoolean("showRateGame", showRateGame);
		prefs.putInteger("gameSessions", gameSessions);
		prefs.flush();
	}
	
	public static void incrementSessions(){
		gameSessions++;
		if(gameSessions == 8) {
			showRateGame = true;
		}
		save();
	}
}

