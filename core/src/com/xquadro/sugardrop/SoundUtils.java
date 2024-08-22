package com.xquadro.sugardrop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundUtils {

	public static void playSound(AssetManager manager, String soundName, float volume){
		if(Settings.soundEnabled) {
			Sound sound = manager.get("sounds/"+soundName, Sound.class);
			sound.play(volume);
		}
	}
	
	public static void playSound(AssetManager manager, String soundName){
		if(Settings.soundEnabled) {
			Sound sound = manager.get("sounds/"+soundName, Sound.class);
			sound.play(0.5f);
		}
	}
	
	public static void playMusic(AssetManager manager){
		if(Settings.musicEnabled) {			
			Music music = manager.get("music/funny_loop.ogg", Music.class);
			music.setVolume(0.7f);
			music.setLooping(true); 
			music.play();
		}
		
	}

	public static void toggleMusic(AssetManager manager){
		Music music = manager.get("music/funny_loop.ogg", Music.class);
		
		if(Settings.musicEnabled && !music.isPlaying() ){
			playMusic(manager);
		}
		
		if(!Settings.musicEnabled && music.isPlaying() ){
			music.stop();
		}	
	}
}
