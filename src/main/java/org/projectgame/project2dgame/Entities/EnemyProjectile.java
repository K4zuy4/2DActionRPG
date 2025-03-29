package org.projectgame.project2dgame.Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class EnemyProjectile {
    private double x, y;
    private final double speed;
    private final double directionX;
    private final double directionY;
    private final ImageView sprite;
    private boolean active = true;

    public EnemyProjectile(double x, double y, double directionX, double directionY, String spritePath, double speed) {
        this.x = x;
        this.y = y;
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;

        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath))));
        double angle = Math.toDegrees(Math.atan2(directionY, directionX));
        this.sprite.setRotate(angle);

        this.sprite.setX(x);
        this.sprite.setY(y);
        this.sprite.setFitWidth(46);
        this.sprite.setFitHeight(10);
    }

    public void update(double deltaTime) {
        x += directionX * speed * deltaTime;
        y += directionY * speed * deltaTime;
        sprite.setX(x);
        sprite.setY(y);
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

    public ImageView getHitbox() {
        return sprite;
    }
}

