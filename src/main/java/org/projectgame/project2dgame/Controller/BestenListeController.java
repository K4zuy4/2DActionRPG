package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Data.TimeWrapper;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class BestenListeController {
    @FXML
    private VBox zeitenBox;

    public void zeigeLevel1() {
        ladeZeiten(1);
    }

    public void zeigeLevel2() {
        ladeZeiten(2);
    }

    public void zeigeLevel3() {
        ladeZeiten(3);
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

            Label label = new Label((i + 1) + ". " + time + "  •  " + date);
            label.getStyleClass().add("zeit-label");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            if (i == 0) label.getStyleClass().add("top1");
            else if (i == 1) label.getStyleClass().add("top2");
            else if (i == 2) label.getStyleClass().add("top3");

            zeitenBox.getChildren().add(label);
        }
    }

    private String formatTime(double seconds) {
        int mins = (int) seconds / 60;
        int secs = (int) seconds % 60;
        int millis = (int) ((seconds - (int) seconds) * 100);

        return String.format("%02d:%02d:%02d", mins, secs, millis);
    }

}
