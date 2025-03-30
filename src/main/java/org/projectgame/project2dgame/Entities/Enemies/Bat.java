package org.projectgame.project2dgame.Entities.Enemies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Bat extends Entity {
    private final ImageView idleGif;
    private final ImageView idleFirstGif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private final ImageView fastRightGif;
    private final ImageView fastLeftGif;
    private ImageView currentSprite;

    private boolean waitingAfterCharge = false;
    private long waitStartTime = 0;
    private final long waitDuration = 1000;

    private boolean charging = false;
    private double chargeDirX = 0;
    private double chargeDirY = 0;

    public Bat(double x, double y, int health, GameField gameField, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 100, gameField, gamePane, entityManagement);

        idleFirstGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-idle_first.gif"))));
        idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-idle.gif"))));
        rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-right.gif"))));
        leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-left.gif"))));
        fastRightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-right-fast.gif"))));
        fastLeftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Bat/bat-left-fast.gif"))));

        this.sprite = idleFirstGif;
        this.currentSprite = idleFirstGif;
        this.sprite.setFitWidth(gameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(gameField.getTileSize() * 1.7);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 255, 255, 0));

        updateHitboxPosition();
    }

    @Override
    public void update(double deltaTime) {
        if (charging) {
            double dx = chargeDirX * getChargeSpeed() * deltaTime;
            double dy = chargeDirY * getChargeSpeed() * deltaTime;

            boolean collisionX = entityManagement.getCollisionCheck().checkCollisionEntity(this.getHitbox(), dx, 0);
            boolean collisionY = entityManagement.getCollisionCheck().checkCollisionEntity(this.getHitbox(), 0, dy);

            if (collisionX || collisionY) {
                stopCharge();
                updateSpriteDirection(0, 0);
                return;
            }

            setX(getX() + dx);
            setY(getY() + dy);
        }
    }

    public void checkStillWaiting() {
        if (waitingAfterCharge) {
            if (System.currentTimeMillis() - waitStartTime >= waitDuration) {
                waitingAfterCharge = false;
                isIdle = true;
            }
        }
    }

    public void startCharge(double dirX, double dirY) {
        charging = true;
        isIdle = false;
        chargeDirX = dirX;
        chargeDirY = dirY;
        updateSpriteDirection(dirX, dirY);
    }

    public void stopCharge() {
        charging = false;
        waitingAfterCharge = true;
        waitStartTime = System.currentTimeMillis();
    }

    public boolean isCharging() {
        return charging;
    }

    public boolean isWaitingAfterCharge() {
        return waitingAfterCharge;
    }

    public double getChargeSpeed() {
        return entitySpeed * 3.5;
    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
        this.sprite.setFitWidth(gameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(gameField.getTileSize() * 1.7);

        if (isCharging()) {
            if (dx > 0 && currentSprite != fastRightGif) {
                setSprite(fastRightGif);
            } else if (dx < 0 && currentSprite != fastLeftGif) {
                setSprite(fastLeftGif);
            }
            return;
        }

        if (!isWaitingAfterCharge()) {
            if (dx > 0 && currentSprite != rightGif) {
                setSprite(rightGif);
            } else if (dx < 0 && currentSprite != leftGif) {
                setSprite(leftGif);
            }
        } else {
            if (currentSprite != idleGif) {
                setSprite(idleGif);
            }
        }
    }


    private void setSprite(ImageView newSprite) {
        if (newSprite == currentSprite) return;

        newSprite.setX(x);
        newSprite.setY(y);
        newSprite.setFitWidth(gameField.getTileSize() * 1.7);
        newSprite.setFitHeight(gameField.getTileSize() * 1.7);

        gamePane.getChildren().remove(currentSprite);
        gamePane.getChildren().add(newSprite);

        this.sprite = newSprite;
        this.currentSprite = newSprite;
    }

    @Override
    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight() - 30));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y + 20);
    }
}