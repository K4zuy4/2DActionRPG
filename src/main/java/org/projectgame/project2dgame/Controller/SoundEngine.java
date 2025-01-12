package org.projectgame.project2dgame.Controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundEngine {
    private final Map<String, AudioClip> soundEffects;
    private MediaPlayer backgroundMusicPlayer;

    public SoundEngine() {
        soundEffects = new HashMap<>();
        loadSoundEffect();
    }

    public void playBackgroundMusic(String musicFilePath) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        Media backgroundMusic = new Media(getClass().getResource(musicFilePath).toExternalForm());

        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.setVolume(0.5); // Lautstärke anpassen (optional)
        backgroundMusicPlayer.play();
    }

    public void loadSoundEffect() {
        /*AudioClip soundEffect = new AudioClip(new File("/resources/Sound/MainMenu.mp3").toURI().toString());
        soundEffects.put("mainMenu", soundEffect);
        AudioClip soundEffect2 = new AudioClip(new File("/resources/Sound/shop.mp3").toURI().toString());
        soundEffects.put("fight", soundEffect2);*/
    }

    public void playSoundEffect(String soundName) {
        AudioClip soundEffect = soundEffects.get(soundName);
        if (soundEffect != null) {
            soundEffect.play();
        } else {
            System.out.println("Sound nicht gefunden: " + soundName);
        }
    }

    // Stoppt alle Sounds und Musik
    public void stopAllSounds() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        for (AudioClip soundEffect : soundEffects.values()) {
            soundEffect.stop();
        }
    }

    public void playMainMenuMusic() {
        playBackgroundMusic("/Sound/MainMenu.mp3");
    }
}

