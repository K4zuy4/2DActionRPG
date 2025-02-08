package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class GameFieldController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    Pane gamePane;

    public Pane getGamePane() {
        return gamePane;
    }
}
