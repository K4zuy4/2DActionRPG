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
        int anzahlLevel = GameField.getLevelCount();

        for (int i = 1; i <= anzahlLevel; i++) {
            levelDropdown.getItems().add("Level " + i);
        }

        levelDropdown.getSelectionModel().selectFirst();

        levelDropdown.setOnAction(e -> {
            String selected = levelDropdown.getValue();
            int level = Integer.parseInt(selected.replace("Level ", ""));
            ladeZeiten(level);
        });

       levelDropdown.setVisibleRowCount(6);
        ladeZeiten(1);
    }

    @FXML
    private void onZurueckButton() {
        levelDropdown.getSelectionModel().selectPrevious();
    }

    @FXML
    private void onWeiterButton() {
        levelDropdown.getSelectionModel().selectNext();
    }

    private void ladeZeiten(int level) {
        zeitenBox.getChildren().clear();

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

    private String formatTime(double seconds) {
        int mins = (int) seconds / 60;
        int secs = (int) seconds % 60;
        int millis = (int) ((seconds - (int) seconds) * 100);

        return String.format("%02d:%02d.%02d", mins, secs, millis);
    }

}
