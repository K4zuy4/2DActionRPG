package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
        // Initialisiert die Level-Auswahl und erstellt Buttons für jedes freigeschaltete Level

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


            // Nur Level freischalten, wenn vorherige abgeschlossen
            if (level > 1 && !CharacterInfo.getLevelDone().contains(level - 1)) {
                levelButton.setDisable(true);
            }

            // Starte ausgewähltes Level
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
    protected void onBackButton() {
        // Schließt das aktuelle Fenster
        Stage stage = (Stage) backButton.getScene().getWindow().getScene().getWindow();
        stage.close();
    }

    private void lvlSelect(int lvl) throws IOException {
        // Schließt das Fenster und öffnet das gewählte Level
        Stage stage = (Stage) backButton.getScene().getWindow().getScene().getWindow();
        stage.close();
        Main.setWindow("GameField", lvl);
    }

    @FXML
    private void onBestenlisteButton() {
        // Öffnet die Bestenliste
        Main.openBestenlisteWindow();
    }
}
