package org.projectgame.project2dgame.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.projectgame.project2dgame.Entities.CharacterInfo;

public class GameFieldController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    Pane gamePane;
    @FXML
    Label geldLabel;
    @FXML
    ImageView imageView;
    @FXML
    Label timeLabel;

    @FXML
    public void initialize() {
        geldLabel.setText("" + CharacterInfo.getMoney());
    }

    public Pane getGamePane() {
        return gamePane;
    }
    public Label getGeldLabel() { return geldLabel; }
    public ImageView getImageView() { return imageView; }
    public Label getTimeLabel() { return timeLabel; }
}
