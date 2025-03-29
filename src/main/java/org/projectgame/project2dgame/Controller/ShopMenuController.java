package org.projectgame.project2dgame.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Entities.CharacterInfo;

public class ShopMenuController {
    @FXML
    Button damageButton;
    @FXML
    Text damageLabel;
    @FXML
    Button speedButton;
    @FXML
    Text speedLabel;
    @FXML
    Button firerateButton;
    @FXML
    Text firerateLabel;
    @FXML
    Button healButton;
    @FXML
    Text healLabel;
    @FXML
    Label geldLabel;

    @FXML
    public void initialize() {
        geldLabel.setText("" + CharacterInfo.getMoney());
        disableButtons();
        damageLabel.setText("Schaden - " + CharacterInfo.getDamagePrice() +"$");
        speedLabel.setText("Speed - " + CharacterInfo.getSpeedPrice() +"$");
        firerateLabel.setText("Feuerrate - " + CharacterInfo.getFireratePrice() +"$");
        healLabel.setText("Heilung - " + CharacterInfo.getHealPrice() +"$");
    }

    public void disableButtons() {

        if(CharacterInfo.getMoney() < CharacterInfo.getHealPrice()) {
            healButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < CharacterInfo.getSpeedPrice()) {
            speedButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < CharacterInfo.getDamagePrice()) {
            damageButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < CharacterInfo.getFireratePrice()) {
            firerateButton.setDisable(true);
        }
    }

    public void setGeldLabel() {
        geldLabel.setText("" + CharacterInfo.getMoney());
    }

    @FXML
    protected void onDamageButton() {
        if(CharacterInfo.getMoney() >= CharacterInfo.getDamagePrice()) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - CharacterInfo.getDamagePrice());
            CharacterInfo.setDamagePrice(CharacterInfo.getDamagePrice() * 2);
            CharacterInfo.setDamage(CharacterInfo.getDamage() + 5);
            damageLabel.setText("Schaden - " + CharacterInfo.getDamagePrice() +"$");
            setGeldLabel();
            disableButtons();
        }
    }

    @FXML
    protected void onSpeedButton() {
        if(CharacterInfo.getMoney() >= CharacterInfo.getSpeedPrice()) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - CharacterInfo.getSpeedPrice());
            CharacterInfo.setSpeedPrice(CharacterInfo.getSpeedPrice() * 2);
            CharacterInfo.setSpeed(CharacterInfo.getSpeed() + 25);
            speedLabel.setText("Speed - " + CharacterInfo.getSpeedPrice() +"$");
            setGeldLabel();
            disableButtons();
        }
    }

    @FXML
    protected void onFirerateButton() {
        if(CharacterInfo.getMoney() >= CharacterInfo.getFireratePrice()) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - CharacterInfo.getFireratePrice());
            CharacterInfo.setFireratePrice(CharacterInfo.getFireratePrice() * 2);
            CharacterInfo.setFireRate(CharacterInfo.getFireRate() - 70);
            firerateLabel.setText("Feuerrate - " + CharacterInfo.getFireratePrice() +"$");
            setGeldLabel();
            disableButtons();
        }
    }

    @FXML
    protected void onHealButton() {
        if(CharacterInfo.getMoney() >= CharacterInfo.getHealPrice()) {

            CharacterInfo.setMoney(CharacterInfo.getMoney() - CharacterInfo.getHealPrice());
            CharacterInfo.setHealPrice(CharacterInfo.getHealPrice() * 2);
            CharacterInfo.setHealth(CharacterInfo.getMaxHealth());
            healLabel.setText("Heilung - " + CharacterInfo.getHealPrice() +"$");
            setGeldLabel();
            disableButtons();
        }
    }

    @FXML
    protected void onExitButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
