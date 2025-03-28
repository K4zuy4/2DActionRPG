package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class LevelSelectionController {
    @FXML
    Button lvl1Button;
    @FXML
    Button lvl2Button;
    @FXML
    Button lvl3Button;
    @FXML
    Button backButton;
    @FXML
    Button bestenlisteButton;

    @FXML
    public void initialize() {
        if (!CharacterInfo.getLevelDone().contains(1)) {
            lvl2Button.setDisable(true);
            lvl3Button.setDisable(true);
        }
        else if (!CharacterInfo.getLevelDone().contains(2)) {
            lvl3Button.setDisable(true);
        }
    }


    @FXML
    protected void onLvl1Button() throws IOException {
        lvlSelect(1);
    }

    @FXML
    protected void onLvl2Button() throws IOException {
        lvlSelect(2);
    }

    @FXML
    protected void onLvl3Button() throws IOException {
        lvlSelect(3);
    }

    @FXML
    protected void onBackButton() throws IOException {
        Main.setWindow("MainMenu", 0);
    }

    private void lvlSelect(int lvl) throws IOException {
        Main.setWindow("GameField", lvl);
    }

    @FXML
    private void onBestenlisteButton() {
        Main.openBestenlisteWindow();
    }
}
