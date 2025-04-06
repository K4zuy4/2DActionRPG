package org.projectgame.project2dgame.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.projectgame.project2dgame.Main;


import java.io.IOException;

public class GameOverEndlessScreenController {

    @FXML
    Button mainButton;
    @FXML
    Button settingsButton;
    @FXML
    Button exitButton;
    @FXML
    Text waveCount;

    @FXML
    private void onMainButton() throws IOException {
        Main.setWindow("MainMenu", 0);
    }

    @FXML
    private void onSettingsButton() throws IOException {
        Main.openSettingsWindow();
    }

    @FXML
    private void onExitButton() {
        Platform.exit();
        System.exit(0);
    }

    public void setWaveCount(int wave) {
        waveCount.setText(String.valueOf(wave - 1));
    }
}
