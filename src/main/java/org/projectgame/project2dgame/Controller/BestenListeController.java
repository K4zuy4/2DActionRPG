package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Data.TimeWrapper;
import org.projectgame.project2dgame.Data.WaveWrapper;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Comparator;
import java.util.List;

public class BestenListeController {
    @FXML
    private VBox zeitenBox;
    @FXML
    private ComboBox<String> levelDropdown;
    @FXML
    private Button zurueckButton;
    @FXML
    private Button weiterButton;

    @FXML
    public void initialize() {
        // Initialisiert das Dropdown-Menü mit Endless Mode und allen Leveln
        // Lädt anschließend die passenden Zeiten/Wellen

        levelDropdown.getItems().add("Endless Mode");

        int anzahlLevel = GameField.getLevelCount();
        for (int i = 1; i <= anzahlLevel; i++) {
            levelDropdown.getItems().add("Level " + i);
        }

        levelDropdown.getSelectionModel().selectFirst();

        levelDropdown.setOnAction(e -> {
            ladeZeiten();
        });

        levelDropdown.setVisibleRowCount(7);
        ladeZeiten();
    }

    @FXML
    private void onZurueckButton() {
        // Navigiert im Dropdown eine Auswahl nach oben
        int selectedIndex = levelDropdown.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            levelDropdown.getSelectionModel().selectPrevious();
        }
    }

    @FXML
    private void onWeiterButton() {
        // Navigiert im Dropdown eine Auswahl nach unten
        int selectedIndex = levelDropdown.getSelectionModel().getSelectedIndex();
        if (selectedIndex < levelDropdown.getItems().size() - 1) {
            levelDropdown.getSelectionModel().selectNext();
        }
    }

    private void ladeZeiten() {
        // Lädt je nach Auswahl entweder:
        // - die besten Endless-Mode-Wellen
        // - oder die Bestzeiten für ein spezifisches Level
        // und zeigt sie in der VBox an

        zeitenBox.getChildren().clear();
        String selected = levelDropdown.getValue();

        if ("Endless Mode".equals(selected)) {
            // --- Wellen laden ---
            List<WaveWrapper> waves = GameSettings.getAllWaves().stream()
                    .sorted(Comparator.comparingInt(WaveWrapper::getWaves).reversed())
                    .toList();

            for (int i = 0; i < waves.size(); i++) {
                WaveWrapper w = waves.get(i);
                String timeFormatted = formatTime(w.getTime());

                Text placementText = new Text((i + 1) + ". ");
                placementText.getStyleClass().add("zeit-text");

                Text waveText = new Text("Wave " + w.getWaves() + " - " + timeFormatted);
                waveText.setUnderline(true);
                waveText.getStyleClass().add("zeit-text");

                Text midText = new Text("  •  ");
                midText.getStyleClass().add("zeit-text");

                Text dateText = new Text(w.getDate());
                dateText.getStyleClass().add("zeit-text");

                placementText.setFill(Color.web("#f2f2f2"));
                waveText.setFill(Color.web("#f2f2f2"));
                midText.setFill(Color.web("#f2f2f2"));
                dateText.setFill(Color.web("#f2f2f2"));

                TextFlow flow = new TextFlow(placementText, waveText, midText, dateText);
                flow.getStyleClass().add("zeit-label");
                flow.setMaxWidth(Double.MAX_VALUE);
                flow.setTextAlignment(TextAlignment.CENTER);

                VBox.setMargin(flow, new Insets(5, 0, 5, 0));

                if (i == 0) flow.getStyleClass().add("top1");
                else if (i == 1) flow.getStyleClass().add("top2");
                else if (i == 2) flow.getStyleClass().add("top3");

                zeitenBox.getChildren().add(flow);
            }

        } else {
            // --- Level-Zeiten laden ---
            int level = Integer.parseInt(selected.replace("Level ", ""));

            List<TimeWrapper> zeiten = GameSettings.getAllTimes().stream()
                    .filter(t -> t.getLevel() == level)
                    .sorted(Comparator.comparingDouble(TimeWrapper::getTime))
                    .toList();

            for (int i = 0; i < zeiten.size(); i++) {
                TimeWrapper t = zeiten.get(i);
                String time = formatTime(t.getTime());
                String date = t.getDate();

                Text placementText = new Text((i + 1) + ". ");
                placementText.getStyleClass().add("zeit-text");

                Text timeText = new Text(time);
                timeText.setUnderline(true);
                timeText.getStyleClass().add("zeit-text");

                Text midText = new Text("  •  ");
                midText.getStyleClass().add("zeit-text");

                Text dateText = new Text(date);
                dateText.getStyleClass().add("zeit-text");

                placementText.setFill(Color.web("#f2f2f2"));
                timeText.setFill(Color.web("#f2f2f2"));
                midText.setFill(Color.web("#f2f2f2"));
                dateText.setFill(Color.web("#f2f2f2"));

                TextFlow flow = new TextFlow(placementText, timeText, midText, dateText);
                flow.getStyleClass().add("zeit-label");
                flow.setMaxWidth(Double.MAX_VALUE);
                flow.setTextAlignment(TextAlignment.CENTER);

                VBox.setMargin(flow, new Insets(5, 0, 5, 0));

                if (i == 0) flow.getStyleClass().add("top1");
                else if (i == 1) flow.getStyleClass().add("top2");
                else if (i == 2) flow.getStyleClass().add("top3");

                zeitenBox.getChildren().add(flow);
            }
        }
    }


    private String formatTime(double seconds) {
        // Formatiert Sekunden in ein mm:ss.SS-Format für eine saubere Anzeige

        int mins = (int) seconds / 60;
        int secs = (int) seconds % 60;
        int millis = (int) ((seconds - (int) seconds) * 100);

        return String.format("%02d:%02d.%02d", mins, secs, millis);
    }
}
