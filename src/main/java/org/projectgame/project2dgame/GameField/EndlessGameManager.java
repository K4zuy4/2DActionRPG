package org.projectgame.project2dgame.GameField;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.Main;

public class EndlessGameManager {
    private static EntityManagement entityManagement;
    private static int waveCount = 0;
    private static boolean waitingForUpgrade = false;
    private static boolean waiting = false;
    private static boolean openedUpgradeWindow = false;


    public EndlessGameManager(EntityManagement entityManagement) {
        this.entityManagement = entityManagement;
    }

    public static int getWaveCount() {
        return waveCount;
    }

    public void startNextWave() {
        if (waiting || waitingForUpgrade) {
            return;
        }

        int temp = waveCount + 1;
        if (temp % 3 == 0 && temp != 0 && !openedUpgradeWindow) {
            openedUpgradeWindow = true;
            waitingForUpgrade = true;
            Main.pauseGameloop(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                Main.openUpgradeWindow();
            });
            pause.play();
        } else {
            openedUpgradeWindow = false;
            waveCount++;
            showWaveLabel(waveCount);
            waiting = true;
            spawnWave();
        }

    }

    private void spawnWave() {
        int slimes = 2 + waveCount / 2;
        int skeletons = 1 + waveCount / 4;
        int bats = (waveCount >= 5) ? 1 + (waveCount - 4) / 5 : 0;

        int slimeHealth = 40 + waveCount * 2;
        int skeletonHealth = 60 + waveCount * 3;
        int batHealth = 50 + waveCount * 3;

        entityManagement.spawnEndlessEntities(slimes, skeletons, bats, slimeHealth, skeletonHealth, batHealth);
    }

    public void checkWaveEnd() {
        if (!waitingForUpgrade && entityManagement.getEntity().isEmpty()) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> startNextWave());
            delay.play();
        }
    }


    public void setWaitingForUpgrade(boolean waiting) {
        this.waitingForUpgrade = waiting;
    }

    public boolean isWaitingForUpgrade() {
        return waitingForUpgrade;
    }


    public static void setWaiting(boolean waiting) {
        EndlessGameManager.waiting = waiting;
    }

    private void showWaveLabel(int waveNumber) {
        Label waveLabel = new Label("Welle " + waveNumber);
        waveLabel.setStyle(
                "-fx-font-size: 60px;" +
                "-fx-font-family: 'Britannic Bold';" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(0,0,0,0.5);" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: center;"
        );
        waveLabel.setPrefWidth(GameField.getScreenWidth());
        waveLabel.setPrefHeight(GameField.getScreenHeight());
        waveLabel.setLayoutX(0);
        waveLabel.setLayoutY(0);
        waveLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Main.getGamePane().getChildren().add(waveLabel);

        // Fade-In
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), waveLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Pause (sichtbar bleiben)
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));

        // Fade-Out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), waveLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Danach Label entfernen
        fadeOut.setOnFinished(event -> Main.getGamePane().getChildren().remove(waveLabel));

        // Reihenfolge abspielen
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(
                fadeIn, visiblePause, fadeOut
        );
        sequence.play();
    }

    public static EntityManagement getEntityManagement() {
        return entityManagement;
    }

}
