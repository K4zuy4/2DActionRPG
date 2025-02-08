package org.projectgame.project2dgame.Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

public class Projectile {
    private double x, y;
    private final double speed = 300;
    private final double directionX;
    private final double directionY;
    private final ImageView sprite;
    private final Rectangle hitbox;
    private boolean active = true;

    public Projectile(double x, double y, double directionX, double directionY, String spritePath, GameField gameField) {
        this.x = x;
        this.y = y;
        this.directionX = directionX;
        this.directionY = directionY;

        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath))));
        this.sprite.setX(x);
        this.sprite.setY(y);

        double hitboxWidth, hitboxHeight;

        if (directionX != 0) {
            hitboxWidth = gameField.getTileSize() * 0.85;
            hitboxHeight = gameField.getTileSize() * 0.45;
        } else {
            hitboxWidth = gameField.getTileSize() * 0.45;
            hitboxHeight = gameField.getTileSize() * 0.85;
        }

        this.hitbox = new Rectangle(x, y, hitboxWidth, hitboxHeight);
    }

    public void update(double deltaTime) {
        x += directionX * speed * deltaTime;
        y += directionY * speed * deltaTime;
        sprite.setX(x);
        sprite.setY(y);

        double spriteWidth = sprite.getImage().getWidth();
        double spriteHeight = sprite.getImage().getHeight();

        double hitboxX = x + (spriteWidth - hitbox.getWidth()) / 2;
        double hitboxY = y + (spriteHeight - hitbox.getHeight()) / 2;

        if (directionX > 0) {
            hitboxX = x + spriteWidth * 0.3;
        } else if (directionX < 0) {
            hitboxX = x + spriteWidth * 0.1;
        }

        if (directionY > 0) {
            hitboxY = y + spriteHeight * 0.3;
        } else if (directionY < 0) {
            hitboxY = y + spriteHeight * 0.1;
        }

        hitbox.setX(hitboxX);
        hitbox.setY(hitboxY);
    }


    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}

