package org.projectgame.project2dgame.Entities;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Enemies.Slime;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Random;

public abstract class Entity {
    protected double x;
    protected double y;
    protected int health;
    protected int maxHealth;
    protected int entitySpeed;
    protected ImageView sprite;
    protected EntityManagement entityManagement;
    protected Pane gamePane;
    protected Rectangle hitbox;
    protected ProgressBar healthBar;
    protected double randomDirectionX = 0;
    protected double randomDirectionY = 0;
    protected long lastRandomMoveTime = 0;
    protected double lastIdleTime = 0;
    protected double durationIdle = 0;
    protected boolean isIdle = false;
    protected boolean isActive = true;
    private boolean waiting = false;
    private PauseTransition currentPause;


    public Entity(double x, double y, int health, int entitySpeed, Pane gamePane, EntityManagement entityManagement) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.entitySpeed = entitySpeed;
        this.entityManagement = entityManagement;
        this.gamePane = gamePane;

        healthBar = new ProgressBar(1);
        healthBar.setPrefWidth(GameField.getTileSize());
        healthBar.setPrefHeight(10);
    }

    public abstract void update(double deltaTime);

    public void takeDamage(int _damage) {
        Random random = new Random();
        int damage = random.nextInt(5 * 2 + 1) + (_damage - 5);
        health -= damage;
        if (health <= 0) {
            health = 0;
            isActive = false;
            entityManagement.removeEntity(this);
        }
        updateHealthBar();
    }

    protected void updateHealthBar() {
        double healthPercent = (double) health / maxHealth;
        healthBar.setProgress(healthPercent);

        String color;
        if (healthPercent > 0.8) color = "#00FF00";
        else if (healthPercent > 0.6) color = "#66FF00";
        else if (healthPercent > 0.4) color = "#FFFF00";
        else if (healthPercent > 0.2) color = "#FF6600";
        else color = "#FF0000";

        Platform.runLater(() -> {
            healthBar.setProgress(healthPercent);
            healthBar.setStyle("-fx-accent: " + color + ";");
        });
    }

    public abstract void updateHitboxPosition();
    protected abstract void updateSpriteDirection(double dx, double dy);

    public void setX(double x) {
        double dx = x - this.x;
        this.x = x;
        sprite.setX(x);
        updateHitboxPosition();

        this.updateSpriteDirection(dx, 0);
    }

    public void setY(double y) {
        double dy = y - this.y;
        this.y = y;
        sprite.setY(y);
        updateHitboxPosition();

        this.updateSpriteDirection(0, dy);
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getEntitySpeed() { return entitySpeed; }
    public ImageView getSprite() { return sprite; }
    public Rectangle getHitbox() { return hitbox; }
    public ProgressBar getHealthBar() { return healthBar; }
    public double getRandomDirectionX() { return randomDirectionX; }
    public double getRandomDirectionY() { return randomDirectionY; }
    public void setRandomDirectionX(double randomDirectionX) { this.randomDirectionX = randomDirectionX; }
    public void setRandomDirectionY(double randomDirectionY) { this.randomDirectionY = randomDirectionY; }
    public long getLastRandomMoveTime() { return lastRandomMoveTime; }
    public void setLastRandomMoveTime(long lastRandomMoveTime) { this.lastRandomMoveTime = lastRandomMoveTime; }
    public void setIdle(boolean bool) { this.isIdle = bool; }
    public PauseTransition getCurrentPause() { return currentPause;}
    public void setCurrentPause(PauseTransition pause) { this.currentPause = pause; }
}
