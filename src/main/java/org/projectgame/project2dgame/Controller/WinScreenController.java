package org.projectgame.project2dgame.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class WinScreenController {
    @FXML
    Button nextButton;
    @FXML
    Button levelSelectButton;
    @FXML
    Button upgradeButton;
    @FXML
    Button exitButton;

    @FXML
    public void initialize() {
        if (CharacterInfo.getLevelDone() >= 3) {
            nextButton.setDisable(true);
        }

        nextButton.setFocusTraversable(false);
        levelSelectButton.setFocusTraversable(false);
        upgradeButton.setFocusTraversable(false);
        exitButton.setFocusTraversable(false);
    }

    @FXML
    protected void onNextButton() throws IOException {
        Main.setWindow("GameField", CharacterInfo.getLevelDone());
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
    protected void onUpgradeButton() {
        try {
            Main.setWindow("UpgradeScreen", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onExitButton() {
        Platform.exit();
        System.exit(0);
    }
}
