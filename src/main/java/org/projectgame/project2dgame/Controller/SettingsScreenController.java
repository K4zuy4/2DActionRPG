package org.projectgame.project2dgame.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Main;

import java.io.IOException;

public class SettingsScreenController {
    @FXML
    Button exitButton;
    @FXML
    Text volumeLabel;
    @FXML
    Slider volumeSlider;
    @FXML
    Button wButton;
    @FXML
    Button aButton;
    @FXML
    Button sButton;
    @FXML
    Button dButton;
    @FXML
    Button obenButton;
    @FXML
    Button untenButton;
    @FXML
    Button linksButton;
    @FXML
    Button rechtsButton;

    @FXML
    public void initialize() {
        volumeSlider.setValue(GameSettings.getVolume() * 100);
        volumeLabel.setText("Lautstärke: " + (int) volumeSlider.getValue() + "%");

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            volumeLabel.setText("Lautstärke: " + newVal.intValue() + "%");
            try {
                GameSettings.setVolume(newVal.doubleValue() / 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        wButton.setText(GameSettings.getKeyMap().get("upKey").toString());
        aButton.setText(GameSettings.getKeyMap().get("leftKey").toString());
        sButton.setText(GameSettings.getKeyMap().get("downKey").toString());
        dButton.setText(GameSettings.getKeyMap().get("rightKey").toString());
        obenButton.setText(GameSettings.getKeyMap().get("lookUpKey").toString());
        untenButton.setText(GameSettings.getKeyMap().get("lookDownKey").toString());
        linksButton.setText(GameSettings.getKeyMap().get("lookLeftKey").toString());
        rechtsButton.setText(GameSettings.getKeyMap().get("lookRightKey").toString());
    }

    @FXML
    protected void onExitButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void changeKey(String key, Button button) {
        button.setText("...");
        button.getScene().getRoot().requestFocus();
        button.getScene().setOnKeyPressed((KeyEvent e) -> {
            KeyCode newKey = e.getCode();
            button.setText(newKey.toString());
            button.getScene().setOnKeyPressed(null);

            try {
                GameSettings.changeKey(key, newKey);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML private void onWButton() { changeKey("upKey", wButton); }
    @FXML private void onAButton() { changeKey("leftKey", aButton); }
    @FXML private void onSButton() { changeKey("downKey", sButton); }
    @FXML private void onDButton() { changeKey("rightKey", dButton); }
    @FXML private void onObenButton() { changeKey("lookUpKey", obenButton); }
    @FXML private void onUntenButton() { changeKey("lookUntenKey", untenButton); }
    @FXML private void onLinksButton() { changeKey("lookLeftKey", linksButton); }
    @FXML private void onRechtsButton() { changeKey("lookRightKey", rechtsButton); }
}
