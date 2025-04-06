package org.projectgame.project2dgame.Entities.Enemies;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Bat extends Entity {
    private final ImageView idleGif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private final ImageView fastRightGif;
    private final ImageView fastLeftGif;
    private ImageView currentSprite;
    private final ImageView spawnGif;

    private boolean waitingAfterCharge = false;
    private long waitStartTime = 0;
    private final long waitDuration = 1000;

    private boolean charging = false;
    private double chargeDirX = 0;
    private double chargeDirY = 0;

    private int slideTicks = 0;
    private final int maxSlideTicks = 20;

    private boolean spawnIdle = true;
    private final long spawnIdleDuration = 1000;
    private long spawnStartTime = System.currentTimeMillis();

    public Bat(double x, double y, int health, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 100, gamePane, entityManagement);

        idleGif = EntityManagement.getImage("bat-idle");
        rightGif = EntityManagement.getImage("bat-right");
        leftGif = EntityManagement.getImage("bat-left");
        fastRightGif = EntityManagement.getImage("bat-right-fast");
        fastLeftGif = EntityManagement.getImage("bat-left-fast");
        spawnGif = EntityManagement.getImage("bat-spawn");

        this.sprite = spawnGif;
        this.currentSprite = spawnGif;

        PauseTransition spawnDelay = new PauseTransition(Duration.seconds(1.5));
        spawnDelay.setOnFinished(e -> {
            this.sprite.setImage(idleGif.getImage());
            this.currentSprite = idleGif;
        });
        spawnDelay.play();

        this.sprite.setFitWidth(GameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(GameField.getTileSize() * 1.7);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, GameField.getTileSize() * 0.7, GameField.getTileSize() * 0.7);
        if(GameField.isDebug()) {
            hitbox.setFill(rgb(107, 68, 0, 0.5));
        } else {
            hitbox.setFill(rgb(255, 0, 0, 0));
        }

        updateHitboxPosition();
    }

    @Override
    public void update(double deltaTime) {
        if (spawnIdle) {
            if (System.currentTimeMillis() - spawnStartTime >= spawnIdleDuration) {
                spawnIdle = false;
            } else {
                return;
            }
        }



        if (charging) {
            double dx = chargeDirX * getChargeSpeed() * deltaTime;
            double dy = chargeDirY * getChargeSpeed() * deltaTime;

            CollisionCheck collisionCheck = entityManagement.getCollisionCheck();

            boolean collisionTileX = collisionCheck.checkCollisionEntity(this.getHitbox(), dx, 0);
            boolean collisionTileY = collisionCheck.checkCollisionEntity(this.getHitbox(), 0, dy);

            boolean collisionEntityX = collisionCheck.checkBatCollisionWithEntity(this.getHitbox(), this, dx, 0);
            boolean collisionEntityY = collisionCheck.checkBatCollisionWithEntity(this.getHitbox(), this, 0, dy);

            boolean collisionX = collisionTileX || collisionEntityX;
            boolean collisionY = collisionTileY || collisionEntityY;

            if (collisionX && collisionY) {
                Character player = entityManagement.getCharacter();
                if (player != null) {
                    double xDiff = player.getX() - this.getX();
                    double yDiff = player.getY() - this.getY();
                    double slideAmount = 20;

                    double newX = getX();
                    double newY = getY();

                    // Slide in X
                    if (Math.abs(xDiff) > Math.abs(yDiff)) {
                        if (xDiff < -10) newX -= slideAmount;
                        else if (xDiff > 10) newX += slideAmount;
                    }

                    // Slide in Y
                    if (Math.abs(yDiff) >= Math.abs(xDiff)) {
                        if (yDiff < -10) newY -= slideAmount;
                        else if (yDiff > 10) newY += slideAmount;
                    }

                    // Check X-Slide
                    if (!collisionCheck.checkCollisionEntity(getHitbox(), newX - getX(), 0) &&
                            !collisionCheck.checkBatCollisionWithEntity(getHitbox(), this, newX - getX(), 0)) {
                        setX(newX);
                    }

                    // Check Y-Slide
                    if (!collisionCheck.checkCollisionEntity(getHitbox(), 0, newY - getY()) &&
                            !collisionCheck.checkBatCollisionWithEntity(getHitbox(), this, 0, newY - getY())) {
                        setY(newY);
                    }
                }

                stopCharge();
                updateSpriteDirection(0, 0);
                slideTicks = 0;
                return;
            }


            if (!collisionX) {
                setX(getX() + dx);
            }

            if (!collisionY) {
                setY(getY() + dy);
            }

            if (collisionX || collisionY) {
                slideTicks++;
            } else {
                slideTicks = 0;
            }

            if (slideTicks >= maxSlideTicks) {
                stopCharge();
                updateSpriteDirection(0, 0);
                slideTicks = 0;
            }
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
        if (Main.getGameLoop() != null && !Main.isGameLoopPaused()) {
            this.sprite.setFitWidth(GameField.getTileSize() * 1.7);
            this.sprite.setFitHeight(GameField.getTileSize() * 1.7);

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
    }


    private void setSprite(ImageView newSprite) {
        if (newSprite == currentSprite) return;

        newSprite.setX(x);
        newSprite.setY(y);
        newSprite.setFitWidth(GameField.getTileSize() * 1.7);
        newSprite.setFitHeight(GameField.getTileSize() * 1.7);

        if(sprite != null) gamePane.getChildren().remove(sprite);
        gamePane.getChildren().remove(currentSprite);
        gamePane.getChildren().add(newSprite);

        this.sprite = newSprite;
        this.currentSprite = newSprite;
    }

    @Override
    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight() - 30));
        healthBar.setLayoutX(x + 20);
        healthBar.setLayoutY(y + 20);
    }
}