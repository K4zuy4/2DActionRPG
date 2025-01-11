package org.projectgame.project2dgame.Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Entity {
    private double x;
    private double y;
    private int health;
    private ImageView sprite;
    private GameField gameField;
    private int entitySpeed = 150;
    private Rectangle hitbox;

    public Entity(double x, double y, int health, String spritePath, GameField gameField) {
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
}