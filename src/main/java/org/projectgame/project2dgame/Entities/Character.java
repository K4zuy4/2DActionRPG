package org.projectgame.project2dgame.Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

public class Character {
    private double x;
    private double y;
    private int health;
    private ImageView sprite;
    private MovementHandler movementHandler;
    private GameField gameField;

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
        movementHandler = new MovementHandler(this);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        this.sprite.setX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        this.sprite.setY(y);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public MovementHandler getMovementHandler() {
        return movementHandler;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void update(double deltaTime) {

    }

    public void render() {

        sprite.setX(x);
        sprite.setY(y);
    }

    public Character getCharacter() {
        return this;
    }
}