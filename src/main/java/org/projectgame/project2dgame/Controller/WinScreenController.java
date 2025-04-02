package org.projectgame.project2dgame.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;
import java.util.ArrayList;

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
        if (CharacterInfo.getLevelDone().contains(GameField.getLevelCount())) {
            nextButton.setDisable(true);
        }

        nextButton.setFocusTraversable(false);
        levelSelectButton.setFocusTraversable(false);
        upgradeButton.setFocusTraversable(false);
        exitButton.setFocusTraversable(false);
    }

    @FXML
    protected void onNextButton() throws IOException {
        ArrayList<Integer> levelDone = CharacterInfo.getLevelDone();
        int max = levelDone.get(0);
        for (int zahl : levelDone) {
            if (zahl > max) {
                max = zahl;
            }
        }
        Main.setWindow("GameField", max + 1);
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
        Main.openUpgradeWindow();
    }

    @FXML
    protected void onExitButton() {
        Platform.exit();
        System.exit(0);
    }
}
