package org.projectgame.project2dgame.Entities;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.projectgame.project2dgame.Controller.SoundEngine;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.rgb;

public class Character {
    private double x;
    private double y;
    private int health = CharacterInfo.getHealth();
    private int maxHealth = CharacterInfo.getMaxHealth();
    private int geld = CharacterInfo.getMoney();
    private ImageView sprite;
    private int characterSpeed = CharacterInfo.getSpeed();
    private final Rectangle hitbox;
    private boolean invincible = false;
    private final ProgressBar healthBar;
    private String direction = "right";
    private long lastAttack = 0;
    private final long cooldown = CharacterInfo.getFireRate();
    private boolean isShooting = false;
    private final EntityManagement entityManagement;
    private final ImageView dyingGif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private final ImageView idleGif;
    private final ImageView idle2Gif;
    private ImageView currentGif;
    private boolean dead = false;

    public Character(double x, double y, EntityManagement entityManagement) {
        this.x = x;
        this.y = y;
        this.entityManagement = entityManagement;

        this.dyingGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/dying.gif"))));
        this.rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/walking_right.gif"))));
        this.leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/walking_left.gif"))));
        this.idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/idle.gif"))));
        this.idle2Gif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/idle2.gif"))));
        this.currentGif = idleGif;

        this.sprite = idleGif;
        this.sprite.setFitHeight(GameField.getTileSize() * 1.5);
        this.sprite.setFitWidth(GameField.getTileSize() * 1.5);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(0, 0, GameField.getTileSize() * 0.7, GameField.getTileSize() * 0.7);
        if(GameField.isDebug()) {
            hitbox.setFill(rgb(255, 0, 0, 0.3));
        } else {
            hitbox.setFill(rgb(255, 0, 0, 0));
        }

        healthBar = new ProgressBar(1);
        healthBar.setPrefWidth(GameField.getTileSize() * 1.5);
        healthBar.setPrefHeight(10);

        updateHealthBar();

        invincible = true;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> Platform.runLater(() -> invincible = false), 2, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y - 20);
    }

    public void takeDamage(int _damage) {
        if (!invincible) {
            SoundEngine.playPlayerHitSound();
            Random random = new Random();
            int damage = random.nextInt(5 * 2 + 1) + (_damage - 5);
            health -= damage;
            if (health < 0) {
                health = 0;
                CharacterInfo.reset();
                Platform.runLater(() -> this.sprite.setImage(dyingGif.getImage()));
                dead = true;
                try {
                    Main.setWindow("GameOver", 0);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
                return;
            }
            CharacterInfo.setHealth(health);
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
        if (dead) return;
        double dx = x - this.x;
        this.x = x;
        this.sprite.setX(x);
        updateHitboxPosition();

        this.updateSpriteDirection(dx, 0);
    }

    public void setY(double y) {
        if(dead) return;
        double dy = y - this.y;
        this.y = y;
        this.sprite.setY(y);
        updateHitboxPosition();

        this.updateSpriteDirection(0, dy);
    }

    public void updateSpriteDirection(double dx, double dy) {
        if (dx != 0) {
            if (dx > 0) {
                if (currentGif != rightGif) {
                    this.sprite.setImage(rightGif.getImage());
                    currentGif = rightGif;
                }
            } else if (dx < 0) {
                if (currentGif != leftGif) {
                    this.sprite.setImage(leftGif.getImage());
                    currentGif = leftGif;
                }
            }
        }

        else if (dy != 0) {
            if (currentGif != rightGif) {
                this.sprite.setImage(rightGif.getImage());
                currentGif = rightGif;
            }
        }
    }


    public void setSpriteToIdle2() {
        if (currentGif != idle2Gif) {
            this.sprite.setImage(idle2Gif.getImage());
            currentGif = idle2Gif;
        }
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
                GameSettings.getKeyMap().get("lookUpKey"), "up",
                GameSettings.getKeyMap().get("lookDownKey"), "down",
                GameSettings.getKeyMap().get("lookLeftKey"), "left",
                GameSettings.getKeyMap().get("lookRightKey"), "right"
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
        CharacterInfo.setMoney(geld);
        entityManagement.getGeldLabel().setText(""+this.geld);
    }

    public int getGeld() {
        return geld;
    }
}
