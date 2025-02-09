package org.projectgame.project2dgame.Entities;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.rgb;
import static org.projectgame.project2dgame.Main.gameSettings;

public class Character {
    private double x;
    private double y;
    private int health;
    private int maxHealth;
    private int geld = 0;
    private ImageView sprite;
    private final GameField gameField;
    private int characterSpeed = 200;
    private final Rectangle hitbox;
    private boolean invincible = false;
    private final ProgressBar healthBar;
    private String direction = "right";
    private long lastAttack = 0;
    private final long cooldown = 500;
    private boolean isShooting = false;
    private final EntityManagement entityManagement;

    public Character(double x, double y, int health, String spritePath, GameField gameField, EntityManagement entityManagement) {
        this.gameField = gameField;
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.entityManagement = entityManagement;

        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath))));
        this.sprite.setFitHeight(gameField.getTileSize() * 1.5);
        this.sprite.setFitWidth(gameField.getTileSize() * 1.5);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(0, 0, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 0, 0, 0.5));
        this.hitbox.setOpacity(0);

        healthBar = new ProgressBar(1);
        healthBar.setPrefWidth(gameField.getTileSize() * 1.5);
        healthBar.setPrefHeight(10);

        updateHealthBar();
    }

    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y - 20);
    }

    public void takeDamage(int damage) {
        if (!invincible) {
            health -= damage;
            if (health < 0){
                health = 0;
                try {
                    Main.setWindow("GameOver", 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            updateHealthBar();
            invincible = true;

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> Platform.runLater(() -> invincible = false), 1, TimeUnit.SECONDS);
            scheduler.shutdown();
        }
    }

    private void updateHealthBar() {
            double healthProzent = (double) health / maxHealth;

            healthBar.setProgress(healthProzent);

            String color;
            if (healthProzent > 0.8) color = "#00FF00";
            else if (healthProzent > 0.6) color = "#66FF00";
            else if (healthProzent > 0.4) color = "#FFFF00";
            else if (healthProzent > 0.2) color = "#FF6600";
            else color = "#FF0000";

            Platform.runLater(() -> {
                healthBar.setProgress(healthProzent);
                healthBar.setStyle("-fx-accent: " + color + ";");
        });
    }

    public void render() {
        sprite.setX(x);
        sprite.setY(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
        this.sprite.setX(x);
        updateHitboxPosition();
    }

    public void setY(double y) {
        this.y = y;
        this.sprite.setY(y);
        updateHitboxPosition();
    }

    public ImageView getSprite() {
        return sprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }

    public int getCharacterSpeed() {
        return characterSpeed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String dir) {
        switch(dir) {
            case "up":
                this.direction = "up";
                break;
            case "down":
                this.direction = "down";
                break;
            case "left":
                this.direction = "left";
                break;
            case "right":
                this.direction = "right";
                break;
        }
    }

    public void updateDirectionQueue(Set<KeyCode> pressedKeys, LinkedList<String> directionQueue) {
        Map<KeyCode, String> keyToDirection = Map.of(
                gameSettings.getKeyMap().get("lookUpKey"), "up",
                gameSettings.getKeyMap().get("lookDownKey"), "down",
                gameSettings.getKeyMap().get("lookLeftKey"), "left",
                gameSettings.getKeyMap().get("lookRightKey"), "right"
        );

        for (var entry : keyToDirection.entrySet()) {
            KeyCode key = entry.getKey();
            String direction = entry.getValue();

            if (pressedKeys.contains(key)) {
                if (!directionQueue.contains(direction)) {
                    directionQueue.add(direction);
                }
            } else {
                directionQueue.remove(direction);
            }
        }
    }

    public long getLastShotTime() {
        return lastAttack;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastAttack = lastShotTime;
    }

    public long getShootCooldown() {
        return cooldown;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    public void setGeld(int geld) {
        this.geld = geld;
        entityManagement.getGeldLabel().setText(""+this.geld);
    }

    public int getGeld() {
        return geld;
    }
}
