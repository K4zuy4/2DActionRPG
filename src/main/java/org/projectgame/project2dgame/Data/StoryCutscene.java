package org.projectgame.project2dgame.Data;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.projectgame.project2dgame.GameField.GameField;

public class StoryCutscene {
    private StackPane cutscenePane;
    private Label storyLabel;

    private Runnable onFinished;

    private String[] storyTexts = {
            "Tief unter den Ruinen einer alten Stadt liegt das Sanctum of Sorrow.",
            "Früher ein Ort des Wissens – heute ein verfluchtes Labyrinth voller Schatten.",
            "Niemand weiß genau, was dort passiert ist. Nur eins ist sicher:",
            "Untote herrschen jetzt über diese Hallen.",
            "Ein uralter Reaper bewacht das Herz des Sanctums – und ruft die Toten, um ihn zu schützen",
            "Du bist der Letzte deines Ordens.",
            "Dein Ziel:",
            "• Überlebe die düsteren Gänge",
            "• Töte alle Untoten, die dir in den Weg kommen",
            "• Finde den Ursprung der Verdorbenheit",
            "• Und stell dich dem Reaper, bevor seine Macht die Außenwelt zerstört",
            "Der Weg wird nicht leicht...",
            "Mit jedem Schritt wirst du tiefer gezogen – und die Schatten werden stärker...",
            "Doch es gibt keinen Weg zurück.",
            "--- TUTORIAL ---",
            "Bewege dich mit W, A, S und D.",
            "Drücke die Pfeiltaste, in die gewünschte Angriffsrichtung, um einen Feuerball abzufeuern.",
            "Viel Glück bei deinem Abenteuer!"
    };

    private int currentIndex = 0;

    public StoryCutscene(Pane parentPane) {
        // Erstelle ein StackPane, das den gesamten Bildschirm abdeckt
        cutscenePane = new StackPane();
        cutscenePane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #000000);");
        cutscenePane.setPrefSize(GameField.getScreenWidth(), GameField.getScreenHeight());

        // Erstelle das Label für den Storytext
        storyLabel = new Label();
        storyLabel.setTextFill(Color.WHITE);
        storyLabel.setFont(new Font("Britannic Bold", 28));
        storyLabel.setWrapText(true);
        storyLabel.setMaxWidth(GameField.getScreenWidth() - 100);
        storyLabel.setAlignment(Pos.CENTER);          // Zentriert den Text im Label
        storyLabel.setTextAlignment(TextAlignment.CENTER); // Zentriert mehrzeiligen Text

        // Füge den Label zum StackPane hinzu – er wird automatisch in der Mitte platziert
        cutscenePane.getChildren().add(storyLabel);

        // Füge das StackPane dem übergeordneten Pane hinzu
        parentPane.getChildren().add(cutscenePane);

        // Restliche Logik bleibt unverändert:
        showNextText();

        cutscenePane.setOnMouseClicked(event -> showNextText());
        cutscenePane.setOnKeyPressed(event -> showNextText());

        Label continueLabel = new Label("Drücke eine beliebige Taste, um fortzufahren...");
        continueLabel.setTextFill(Color.GRAY);
        continueLabel.setFont(new Font("Consolas", 18));
        // Positioniere das Continue-Label unten (alternativ in ein BorderPane oder ähnliches einbetten)
        continueLabel.setTranslateY(GameField.getScreenHeight() / 2 - 50);
        cutscenePane.getChildren().add(continueLabel);

        cutscenePane.requestFocus();
    }

    private void showNextText() {
        if (currentIndex < storyTexts.length) {
            storyLabel.setText(storyTexts[currentIndex]);
            currentIndex++;
        } else {
            endCutscene();
        }
    }

    public void endCutscene() {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), cutscenePane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            cutscenePane.setOpacity(0.0);
            cutscenePane.setVisible(false);
            ((Pane)cutscenePane.getParent()).getChildren().remove(cutscenePane);
            if (onFinished != null) {
                onFinished.run();
            }
        });
        fade.play();
    }



    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
