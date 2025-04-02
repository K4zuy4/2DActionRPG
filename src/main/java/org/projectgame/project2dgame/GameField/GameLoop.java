package org.projectgame.project2dgame.GameField;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.Main;

import java.util.LinkedList;
import java.util.Set;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private double totalGameTime = 0;
    private double timeSinceLastLabelUpdate = 0;
    private Label timeLabel;
    private final EntityManagement entityManagement;
    private final KeyInputHandler keyInputHandler;
    private final CollisionCheck collisionCheck;
    private final boolean FPSAnzeigen = false;
    private boolean running = true;
    private final Character character;
    private final LinkedList<String> directionQueue = new LinkedList<>();
    private long lastSpikeDamageTime = 0;
    private static final long DAMAGE_COOLDOWN_MS = 500;
    private boolean paused = false;


    public GameLoop(EntityManagement entityManagement, KeyInputHandler keyInputHandler, CollisionCheck collisionCheck, Label timeLabel) {
        this.entityManagement = entityManagement;
        this.keyInputHandler = keyInputHandler;
        this.collisionCheck = collisionCheck;
        this.entityManagement.setCollisonCheck(collisionCheck);
        this.character = entityManagement.getCharacter();
        this.entityManagement.createProjectileManagement();
        this.timeLabel = timeLabel;
    }

    @Override
    public void handle(long now) {
        if (!running) {
            stop();
            return;
        }

        if(lastUpdate == 0) {
            lastUpdate = now;
            lastFpsUpdate = now;
            return;
        }

        double lastFrame = (now - lastUpdate) / 1_000_000_000.0; //Nanosekunde zu Sekunde
        frameCount++;

        if (now - lastFpsUpdate >= 1_000_000_000) { //FPS anzeigen
            int fps = frameCount;
            if(FPSAnzeigen) System.out.println("FPS: " + fps);
            frameCount = 0;
            lastFpsUpdate = now;
        }

        update(lastFrame);
        render();

        lastUpdate = now;
    }

    public void update(double lastFrame) {
        if(!paused) {
            totalGameTime += lastFrame;
            timeSinceLastLabelUpdate += lastFrame;

            if (timeLabel != null && timeSinceLastLabelUpdate >= 1.0) {
                timeSinceLastLabelUpdate = 0;

                String formattedTime = getFormattedGameTime();

                javafx.application.Platform.runLater(() -> {
                    timeLabel.setText("Zeit: " + formattedTime);
                });
            }


            entityManagement.updateEntities(lastFrame, collisionCheck);
            updateCharacterMovement(lastFrame);
            entityManagement.getProjectileManagement().updateProjectiles(lastFrame);

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSpikeDamageTime >= DAMAGE_COOLDOWN_MS) {
                Rectangle playerHitbox = character.getHitbox();
                if (playerHitbox != null) {
                    collisionCheck.checkDamageTiles(playerHitbox);
                    lastSpikeDamageTime = currentTime;
                }
            }
        }
    }

    public void render() {
        entityManagement.renderEntities();
        entityManagement.renderCharacter();
        entityManagement.getCharacter().updateHitboxPosition();
    }

    private void updateCharacterMovement(double deltaTime) {
        Set<KeyCode> pressedKeys = keyInputHandler.getPressedKeys();
        double distance = character.getCharacterSpeed() * deltaTime;
        boolean moved = false;

        if (entityManagement.getCharacter() != null) {
            entityManagement.getCharacter().updateDirectionQueue(pressedKeys, directionQueue);

            if (!directionQueue.isEmpty()) {
                String newDirection = directionQueue.getLast();
                if (!newDirection.equals(entityManagement.getCharacter().getDirection())) {
                    entityManagement.getCharacter().setDirection(newDirection);
                }
            }

            if (pressedKeys.contains(GameSettings.getKeyMap().get("upKey"))) {
                moveCharacter(0, -distance);
                moved = true;
            }
            if (pressedKeys.contains(GameSettings.getKeyMap().get("downKey"))) {
                moveCharacter(0, distance);
                moved = true;
            }
            if (pressedKeys.contains(GameSettings.getKeyMap().get("leftKey"))) {
                moveCharacter(-distance, 0);
                moved = true;
            }
            if (pressedKeys.contains(GameSettings.getKeyMap().get("rightKey"))) {
                moveCharacter(distance, 0);
                moved = true;
            }
            if(pressedKeys.contains(KeyCode.ESCAPE)) {
                Main.pauseGame();
                pressedKeys.remove(KeyCode.ESCAPE);
            }

            long currentTime = System.currentTimeMillis();

            if (pressedKeys.contains(GameSettings.getKeyMap().get("lookUpKey")) || pressedKeys.contains(GameSettings.getKeyMap().get("lookDownKey")) || pressedKeys.contains(GameSettings.getKeyMap().get("lookRightKey")) || pressedKeys.contains(GameSettings.getKeyMap().get("lookLeftKey"))) {
                character.setShooting(true);
                if (character.isShooting() && (currentTime - character.getLastShotTime() >= character.getShootCooldown())) {
                    entityManagement.getProjectileManagement().characterProjectile();
                    character.setLastShotTime(currentTime);
                }
            } else {
                character.setShooting(false);
            }
        }

        if (!moved) {
            character.setSpriteToIdle2();
        }
    }


    private void moveCharacter(double dx, double dy) {
        double newX = character.getX() + dx;
        double newY = character.getY() + dy;
        double newHitboxX = character.getHitbox().getX() + dx;
        double newHitboxY = character.getHitbox().getY() + dy;
        Rectangle newHitbox = new Rectangle(newHitboxX, newHitboxY, character.getHitbox().getWidth(), character.getHitbox().getHeight());
        newHitbox.setFill(Color.RED);

        if(!collisionCheck.checkCollision(newHitbox)) {
            character.setX(newX);
            character.setY(newY);
        }
    }

    public String getFormattedGameTime() {
        int seconds = (int) totalGameTime % 60;
        int minutes = (int) (totalGameTime / 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public double getRawGameTimeSeconds() {
        return totalGameTime;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}