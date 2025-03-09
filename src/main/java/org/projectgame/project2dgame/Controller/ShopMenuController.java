package org.projectgame.project2dgame.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Main;

public class ShopMenuController {
    @FXML
    Button damageButton;
    @FXML
    Button speedButton;
    @FXML
    Button firerateButton;
    @FXML
    Button healButton;
    @FXML
    Label geldLabel;

    @FXML
    public void initialize() {
        geldLabel.setText("" + CharacterInfo.getMoney());
        disableButtons();
    }

    public void disableButtons() {

        if(CharacterInfo.getMoney() < 25) {
            healButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < 30) {
            speedButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < 45) {
            damageButton.setDisable(true);
        }

        if(CharacterInfo.getMoney() < 60) {
            firerateButton.setDisable(true);
        }
    }

    public void setGeldLabel() {
        geldLabel.setText("" + CharacterInfo.getMoney());
    }

    @FXML
    protected void onDamageButton() {
        if(CharacterInfo.getMoney() >= 45) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - 45);
            CharacterInfo.setDamage(CharacterInfo.getDamage() + 2);
            setGeldLabel();
            disableButtons();
        }
        System.out.println("Damage");
    }

    @FXML
    protected void onSpeedButton() {
        if(CharacterInfo.getMoney() >= 30) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - 30);
            CharacterInfo.setSpeed(CharacterInfo.getSpeed() + 50);
            setGeldLabel();
            disableButtons();
        }
        System.out.println("Speed");
    }

    @FXML
    protected void onFirerateButton() {
        if(CharacterInfo.getMoney() >= 60) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - 60);
            CharacterInfo.setFireRate(CharacterInfo.getFireRate() - 100);
            setGeldLabel();
            disableButtons();
        }
        System.out.println("Firerate");
    }

    @FXML
    protected void onHealButton() {
        if(CharacterInfo.getMoney() >= 25) {
            CharacterInfo.setMoney(CharacterInfo.getMoney() - 25);
            CharacterInfo.setHealth(CharacterInfo.getMaxHealth());
            setGeldLabel();
            disableButtons();
        }
        System.out.println("Heal");
    }

    @FXML
    protected void onExitButton() {
        try {
            Main.setWindow("Win", 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Back");
    }
}
