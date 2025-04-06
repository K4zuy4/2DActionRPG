package org.projectgame.project2dgame.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class PauseMenuController {
    @FXML
    Button weiterButton;
    @FXML
    Button mainButton;
    @FXML
    Button settingsButton;

    @FXML
    protected void onWeiterButton() {
        Main.pauseGameloop(false);
        if (Main.getPauseStage() != null) {
            Main.getPauseStage().close();
        }
        Main.getPausePane().setVisible(false);
    }

    @FXML
    protected void onMainButton() {
        try {
            CharacterInfo.reset();
            Main.setWindow("MainMenu", 0);
            Stage stage = (Stage) weiterButton.getScene().getWindow().getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSettingsButton() {
        Main.openSettingsWindow();
    }
}
