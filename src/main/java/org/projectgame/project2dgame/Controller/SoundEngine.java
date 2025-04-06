package org.projectgame.project2dgame.Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.projectgame.project2dgame.Data.GameSettings;

import java.util.Objects;

public class SoundEngine {
    private static MediaPlayer backgroundMusicPlayer;
    private static MediaPlayer soundPlayer;
    private static MediaPlayer AmbientSoundPlayer;
    private static double lautstaerke = GameSettings.getVolume();

    private SoundEngine() {}

    public static void playBackgroundMusic(String musicFilePath) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        if (AmbientSoundPlayer != null) {
            AmbientSoundPlayer.stop();
        }

        Media backgroundMusic = new Media(Objects.requireNonNull(SoundEngine.class.getResource(musicFilePath)).toExternalForm());

        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.setVolume(lautstaerke);
        backgroundMusicPlayer.play();
    }

    public static void playSound(String soundFilePath) {
        Media sound = new Media(Objects.requireNonNull(SoundEngine.class.getResource(soundFilePath)).toExternalForm());

        soundPlayer = new MediaPlayer(sound);
        soundPlayer.setCycleCount(1);
        soundPlayer.setVolume(lautstaerke);
        soundPlayer.play();
    }

    public static void playAmbientSound(String soundFilePath) {
        if (AmbientSoundPlayer != null) {
            AmbientSoundPlayer.stop();
        }

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        Media sound = new Media(Objects.requireNonNull(SoundEngine.class.getResource(soundFilePath)).toExternalForm());

        AmbientSoundPlayer = new MediaPlayer(sound);
        AmbientSoundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        AmbientSoundPlayer.setVolume(lautstaerke * 2);
        AmbientSoundPlayer.play();
    }

    public static void playMainMenuMusic() {
        playBackgroundMusic("/Sound/MainMenu.mp3");
    }

    public static void playFightMusic() {
        playBackgroundMusic("/Sound/FightSound.mp3");
    }

    public static void playShopMusic() {
        playBackgroundMusic("/Sound/shop.mp3");
    }

    public static void playGameOver() {
        playBackgroundMusic("/Sound/GameOver.mp3");
        backgroundMusicPlayer.setCycleCount(1);
    }

    public static void playWin() {
        playBackgroundMusic("/Sound/win.mp3");
        backgroundMusicPlayer.setCycleCount(1);
    }

    public static void playEnemyHitSound() {
        playSound("/Sound/enemy_hit.mp3");
        soundPlayer.setCycleCount(1);
    }

    public static void playPlayerHitSound() {
        playSound("/Sound/player_hit.mp3");
        soundPlayer.setCycleCount(1);
    }

    public static void playAmbientSound() {
        playAmbientSound("/Sound/DarkAmbient.mp3");
    }

    public static void playBossMusic() {
        playBackgroundMusic("/Sound/BossFight.mp3");
    }
    public static void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    public static void changeVolume(double volume) {
        lautstaerke = volume;
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(volume);
        }
    }

    public static double getVolume() {
        return lautstaerke;
    }

    public static boolean isPlaying() {
        return backgroundMusicPlayer != null &&
                backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

}
