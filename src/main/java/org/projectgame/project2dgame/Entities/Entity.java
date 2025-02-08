package org.projectgame.project2dgame.Entities;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Entity {
    private double x;
    private double y;
    private int health;
    private int maxHealth;
    private ImageView sprite;
    private GameField gameField;
    private int entitySpeed = 100;
    private Rectangle hitbox;
    private ProgressBar healthBar;

    public Entity(double x, double y, int health, String spritePath, GameField gameField) {
        this.gameField = gameField;
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath))));
        this.sprite.setFitHeight(gameField.getTileSize() * 1.5);
        this.sprite.setFitWidth(gameField.getTileSize() * 1.5);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(0, 0, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 0, 0, 0.5));
        this.hitbox.setOpacity(0);

        healthBar = new ProgressBar(1);
        healthBar.setPrefWidth(gameField.getTileSize());
        healthBar.setPrefHeight(8);
        healthBar.getStylesheets().add(getClass().getResource("/css/healthbar.css").toExternalForm());


        updateHealthBar();
    }

    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y - 20);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
        }
        updateHealthBar();
    }

    private void updateHealthBar() {
        double healthProzent = (double) health / maxHealth;

        healthBar.setProgress(healthProzent);

        String color;
        if (healthProzent > 0.8) color = "#00FF00";
        else if (healthProzent > 0.6) color = "#66FF00";
        else if (healthProzent > 0.4) color = "#FFFF00";
        else if (healthProzent > 0.2) color = "#FF6600";
        else color = "#FF0000";

        Platform.runLater(() -> {
            healthBar.setProgress(healthProzent);
            healthBar.setStyle("-fx-accent: " + color + ";");
        });
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        this.sprite.setX(x);
        updateHitboxPosition();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        this.sprite.setY(y);
        updateHitboxPosition();
    }

    public int getEntitySpeed() {
        return entitySpeed;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void render() {
        sprite.setX(x);
        sprite.setY(y);
    }

    public Entity getEntity() {
        return this;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }
}