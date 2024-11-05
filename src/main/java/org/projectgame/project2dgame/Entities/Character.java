package org.projectgame.project2dgame.Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Character {
    private double x;
    private double y;
    private int health;
    private ImageView sprite;
    private MovementHandler movementHandler;
    private GameField gameField;
    private int characterSpeed = 200;
    private Rectangle hitbox;
    private boolean collisionOn = false;

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

        this.hitbox = new Rectangle(0, 0, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 0, 0, 0.5));
    }

    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight())); // zentriert die Hitbox vertikal
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

    public int getHealth() {
        return health;
    }

    public int getCharacterSpeed() {
        return characterSpeed;
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
}