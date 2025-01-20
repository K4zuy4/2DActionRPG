package org.projectgame.project2dgame.Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEngine {
    private MediaPlayer backgroundMusicPlayer;
    private double lautstaerke = 0.5;

    public SoundEngine() {

    }

    public void playBackgroundMusic(String musicFilePath) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        Media backgroundMusic = new Media(getClass().getResource(musicFilePath).toExternalForm());

        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.setVolume(lautstaerke);
        backgroundMusicPlayer.play();
    }


    public void playMainMenuMusic() {
        playBackgroundMusic("/Sound/MainMenu.mp3");
    }

    public void playFightMusic() {
        playBackgroundMusic("/Sound/FightSound.mp3");
    }
}

