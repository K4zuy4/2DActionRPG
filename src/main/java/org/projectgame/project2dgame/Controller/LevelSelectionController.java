package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class LevelSelectionController {
    @FXML
    GridPane levelGrid;
    @FXML
    Button backButton;
    @FXML
    Button bestenlisteButton;

    @FXML
    public void initialize() {
        int totalLevels = GameField.getLevelCount();
        int columns = 3;

        levelGrid.setHgap(5);
        levelGrid.setVgap(25);

        for (int i = 0; i < totalLevels; i++) {
            int level = i + 1;
            Button levelButton = new Button("Level " + level);
            levelButton.getStyleClass().add("menu-button2");
            levelButton.setPrefSize(120, 60);
            levelButton.setPrefWidth(120);
            levelButton.setMinWidth(60);


            if (level > 1 && !CharacterInfo.getLevelDone().contains(level - 1)) {
                levelButton.setDisable(true);
            }

            levelButton.setOnAction(e -> {
                try {
                    lvlSelect(level);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            int row = i / columns;
            int col = i % columns;
            levelGrid.add(levelButton, col, row);
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
