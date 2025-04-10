package org.projectgame.project2dgame.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class MainMenuController {
    @FXML
    Button levelSelectButton;
    @FXML
    Button endlessModeButton;
    @FXML
    Button settingsButton;
    @FXML
    Button exitButton;

    @FXML
    protected void onLevelSelectButton() throws IOException {
        Main.setWindow("LevelSelect", 0);
    }

    @FXML
    protected void onEndlessModeButton() {
        try {
            Main.startEndlessMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onSettingsButton(){
        Main.openSettingsWindow();
    }

    @FXML
    protected void onExitButton() {
        Platform.exit();
        System.exit(0);
    }
}
