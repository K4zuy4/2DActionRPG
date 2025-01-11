package org.projectgame.project2dgame.Entities;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.rgb;

public class Character {
    private double x;
    private double y;
    private int health;
    private ImageView sprite;
    private GameField gameField;
    private int characterSpeed = 200;
    private Rectangle hitbox;

    public Character(double x, double y, int health, String spritePath, GameField gameField) {
        this.gameField = gameField;
        this.x = x;
        this.y = y;
        this.health = health;
        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath))));
        this.sprite.setFitHeight(gameField.getTileSize() * 1.5);
        this.sprite.setFitWidth(gameField.getTileSize() * 1.5);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(0, 0, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 0, 0, 0.5));
        //this.hitbox.setFill(null);
    }

    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()));
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

    public int getCharacterSpeed() {
        return characterSpeed;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void render() {
        sprite.setX(x);
        sprite.setY(y);
    }

    public Character getCharacter() {
        return this;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public void setCharacterSpeed() {
        characterSpeed += 50;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.schedule(() -> Platform.runLater(() -> {
            characterSpeed -= 50;
        }), 2, TimeUnit.SECONDS);

        scheduler.shutdown();
    }
}