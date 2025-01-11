package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.projectgame.project2dgame.GameField.GameLoop;

public class GameFieldController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    Pane gamePane;
    private GameLoop gameLoop;

    public Pane getGamePane() {
        return gamePane;
    }

    public void endLevel() {
        if (gameLoop != null) {
            gameLoop.stopLoop();
        }


    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }
}
