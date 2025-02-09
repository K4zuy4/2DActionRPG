package org.projectgame.project2dgame.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class GameOverScreenController {
    @FXML
    Button retryButton;
    @FXML
    Button levelSelectButton;
    @FXML
    Button settingsButton;
    @FXML
    Button exitButton;

    @FXML
    protected void onRetryButton() throws IOException {
        Main.setWindow("GameField", Main.getGameField().getLevel());
    }

    @FXML
    protected void onLevelSelectButton() {
        try {
            Main.setWindow("LevelSelect", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSettingsButton() {

    }

    @FXML
    protected void onExitButton() {
        Platform.exit();
        System.exit(0);
    }
}
