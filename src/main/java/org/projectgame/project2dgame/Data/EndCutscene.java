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

public class EndCutscene {
    private StackPane cutscenePane;
    private Label storyLabel;

    private Runnable onFinished;

    private String[] storyTexts = {
            "Die letzten Schatten des Sanctum of Sorrow weichen langsam.",
            "Mit jedem gefallenen Untoten verblasst auch der dunkle Fluch.",
            "Die verfluchten Ruinen erstrahlen in einem schwachen Licht – ein Zeichen des beginnenden Neubeginns.",
            "Dein unerschütterlicher Mut hat den Bann gebrochen.",
            "In der Stille folgt die Hoffnung: Ein neuer Morgen bricht an.",
            "Dein Opfer war nicht umsonst – das Licht kehrt zurück.",
            "Vielen Dank, dass du dieses Abenteuer gespielt hast!",
            "Drücke eine beliebige Taste, um zum Hauptmenü zurückzukehren."
    };


    private int currentIndex = 0;

    public EndCutscene(Pane parentPane) {
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
