package org.projectgame.project2dgame.Entities.Enemies;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;
import java.util.Random;

public class Skeleton extends Entity {
    private final ImageView leftGif;
    private final ImageView rightGif;
    private final ImageView attackRightGif;
    private final ImageView attackLeftGif;
    private final ImageView idleGif;
    private final ImageView idleFirstGif;
    private ImageView currentSprite;
    private boolean isAttacking = false;
    private long lastAttackTime = 0;
    private final long attackCooldown = 3000;
    private int shotsFired = 0;
    private boolean retreating = false;
    private long retreatStartTime = 0;
    private final long retreatDuration;
    private boolean inCooldown = false;
    private long cooldownStartTime = 0;
    private final long cooldownDuration;

    public Skeleton(double x, double y, int health, GameField gameField, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 70, gameField, gamePane, entityManagement);

        Random ran = new Random();
        cooldownDuration = ran.nextInt(500, 1000);
        retreatDuration = ran.nextInt(500, 1000);

        leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-left.gif"))));
        rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-right.gif"))));
        attackRightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-attack_right.gif"))));
        attackLeftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-attack_left.gif"))));
        idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-idle.gif"))));
        idleFirstGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Skeleton/skeleton-idle_first.gif"))));
        currentSprite = idleFirstGif;

        this.sprite = currentSprite;
        this.sprite.setFitWidth(gameField.getTileSize() * 2.5);
        this.sprite.setFitHeight(gameField.getTileSize() * 2.5);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setOpacity(0);

        updateHitboxPosition();
    }

    @Override
    public void update(double deltaTime) {
        long now = System.currentTimeMillis();
        if (!isAttacking && now - lastAttackTime >= attackCooldown) {
            startAttack();
        }
    }

    public void startAttack() {
        isAttacking = true;
        lastAttackTime = System.currentTimeMillis();

        double dx = entityManagement.getCharacter().getX() - this.getX();
        this.updateSpriteDirection(dx, 0);

        new Thread(() -> {
            try {
                Thread.sleep(1150);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                if (!isActive) return;

                if (entityManagement != null && entityManagement.getProjectileManagement() != null) {
                    double startX = this.getX() + this.getSprite().getFitWidth() / 2;
                    double startY = this.getY() + this.getSprite().getFitHeight() / 2;
                    double targetX = entityManagement.getCharacter().getX();
                    double targetY = entityManagement.getCharacter().getY();

                    entityManagement.getProjectileManagement().spawnEnemyArrow(startX, startY, targetX, targetY);
                }

                isAttacking = false;
            });

        }).start();
    }


    @Override
    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight() - 50));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y + 20);
    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
        if(isIdle) {
            if (currentSprite != idleGif) {
                setSprite(idleGif);
            }
        } else if (isAttacking) {
            if (dx > 0) {
                if (currentSprite != attackRightGif) {
                    setSprite(attackRightGif);
                    currentSprite = attackRightGif;
                }
            } else {
                if (currentSprite != attackLeftGif) {
                    setSprite(attackLeftGif);
                    currentSprite = attackLeftGif;
                }
            }
        } else {
            if (dx > 0) {
                if (currentSprite != rightGif) {
                    setSprite(rightGif);
                }
            } else if (dx < 0) {
                if (currentSprite != leftGif) {
                    setSprite(leftGif);
                }
            } else if (inCooldown){
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
        newSprite.setFitWidth(gameField.getTileSize() * 2.5);
        newSprite.setFitHeight(gameField.getTileSize() * 2.5);

        gamePane.getChildren().remove(currentSprite);
        gamePane.getChildren().add(newSprite);

        this.sprite = newSprite;
        this.currentSprite = newSprite;
    }


    public boolean isAttacking() {
        return isAttacking;
    }

    public void startRetreat() {
        retreating = true;
        retreatStartTime = System.currentTimeMillis();
        shotsFired = 0;
    }

    public boolean isRetreating() {
        return retreating;
    }

    public long getRetreatStartTime() {
        return retreatStartTime;
    }

    public long getRetreatDuration() {
        return retreatDuration;
    }

    public void resetRetreat() {
        retreating = false;
        inCooldown = true;
        cooldownStartTime = System.currentTimeMillis();
    }


    public int getShotsFired() {
        return shotsFired;
    }

    public void incrementShotsFired() {
        shotsFired++;
    }

    public boolean isInCooldown() {
        return inCooldown;
    }

    public long getCooldownStartTime() {
        return cooldownStartTime;
    }

    public long getCooldownDuration() {
        return cooldownDuration;
    }

    public void endCooldown() {
        inCooldown = false;
    }
}
