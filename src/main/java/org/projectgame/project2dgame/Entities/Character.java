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
import org.projectgame.project2dgame.GameField.EndlessGameManager;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.rgb;

public class Character {
    // --- Basisattribute ---
    private double x, y;
    private int health = CharacterInfo.getHealth();
    private int maxHealth = CharacterInfo.getMaxHealth();
    private int geld = CharacterInfo.getMoney();
    private int characterSpeed = CharacterInfo.getSpeed();
    private boolean invincible = false;
    private boolean dead = false;
    private boolean isSpawning = false;

    private long lastAttack = 0;
    private long cooldown = CharacterInfo.getFireRate();
    private boolean isShooting = false;

    private String direction = "right";
    private final EntityManagement entityManagement;

    // --- Grafiken & Animationen ---
    private ImageView sprite;
    private final ImageView dyingGif, rightGif, leftGif, idleGif, spawnGif, spawnGif2, spawnStill;
    private ImageView currentGif;
    private PauseTransition spawnDelay;

    // --- Hitbox & Lebensanzeige ---
    private final Rectangle hitbox;
    private final ProgressBar healthBar;

    public Character(double x, double y, EntityManagement entityManagement) {
        // Initialisiert Position, Entity-Management, Lade Grafiken & Setup Hitbox
        // Startet Spawn-Animation und Unverwundbarkeit


        this.x = x;
        this.y = y;
        this.entityManagement = entityManagement;

        this.dyingGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/dying.gif"))));
        this.rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/walking_right.gif"))));
        this.leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/walking_left.gif"))));
        this.idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/idle.gif"))));
        this.spawnGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/spawn.gif"))));
        this.spawnGif2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/spawn2.gif"))));
        this.spawnStill = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Main Character/spawnStill.png"))));
        this.currentGif = spawnGif;

        this.sprite = spawnGif;

        spawnDelay = new PauseTransition(Duration.seconds(2.1));
        spawnDelay.setOnFinished(e -> {
            this.sprite.setImage(idleGif.getImage());
            this.currentGif = idleGif;
        });
        spawnDelay.play();

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
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y - 20);

        updateHealthBar();

        invincible = true;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> Platform.runLater(() -> invincible = false), 2, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    public void reload() {
        // Setzt Werte nach einem Level- oder Upgrade-Reset neu

        cooldown = CharacterInfo.getFireRate();
        characterSpeed = CharacterInfo.getSpeed();
        health = CharacterInfo.getHealth();
        maxHealth = CharacterInfo.getMaxHealth();
        geld = CharacterInfo.getMoney();

        updateHitboxPosition();
        updateHealthBar();
    }

    public void updateHitboxPosition() {
        // Aktualisiert Hitbox- und Healthbar-Position anhand von Spielerposition
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y - 20);
    }

    public void takeDamage(int _damage, boolean bossDamage) {
        // Verarbeitet Schaden: reduziert HP, startet Tod oder Unverwundbarkeit
        if ((!invincible || bossDamage) && !dead) {
            SoundEngine.playPlayerHitSound();
            Random random = new Random();
            int damage = random.nextInt(5 * 2 + 1) + (_damage - 5);
            health -= damage;
            if (health <= 0 && !dead) {
                health = 0;
                dead = true;
                Platform.runLater(() -> this.sprite.setImage(dyingGif.getImage()));

                try {
                    CharacterInfo.reset();
                    reload();
                    if (Main.isEndlessMode()) {
                        Main.safeEndlessGameTime(EndlessGameManager.getWaveCount() - 1);

                        Main.setWindow("GameOverEndless", 0);
                    } else {
                        Main.setWindow("GameOver", 0);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
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


    public void setSpawnStill() {
        // Zeigt stillstehende Spawn-Grafik an
        spawnDelay.stop();
        this.currentGif = spawnGif2;
        this.sprite.setImage(spawnStill.getImage());
    }

    public void playSpawnAnimation() {
        // Spielt Spawn-Animation und geht danach in Idle-Animation über
        isSpawning = true;
        this.currentGif = spawnGif2;
        this.sprite.setImage(EntityManagement.getImage("player-spawn").getImage());

        PauseTransition idleDelay = new PauseTransition(Duration.seconds(2.1));
        idleDelay.setOnFinished(e -> {
            this.sprite.setImage(idleGif.getImage());
            this.currentGif = idleGif;
            isSpawning = false;  // Animation beendet
        });
        idleDelay.play();
    }



    private void updateHealthBar() {
        // Aktualisiert Lebensanzeige visuell je nach aktuellem HP-Wert
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
        // Synchronisiert Sprite-Position mit Spielerposition
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
        // Hält die aktuelle Tastenreihenfolge aktuell (für Blickrichtung unabhängig von Bewegung)
        if(!Main.isGameLoopPaused()) {
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
    }


    public void setSpriteToIdle2() {
        if (Main.getGameLoop() != null && !Main.isGameLoopPaused() && !isSpawning) {
            if (currentGif != idleGif) {
                this.sprite.setImage(idleGif.getImage());
                currentGif = idleGif;
            }
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
        // Hält die aktuelle Tastenreihenfolge aktuell (für Blickrichtung unabhängig von Bewegung)
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
