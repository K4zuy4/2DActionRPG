package org.projectgame.project2dgame.GameField;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.EntityManagement;

import java.util.LinkedList;
import java.util.Set;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private final EntityManagement entityManagement;
    private final KeyInputHandler keyInputHandler;
    private final CollisionCheck collisionCheck;
    private final GameSettings gameSettings;
    private final boolean FPSAnzeigen = false;
    private boolean running = true;
    private final Character character;
    private final LinkedList<String> directionQueue = new LinkedList<>();

    public GameLoop(EntityManagement entityManagement, KeyInputHandler keyInputHandler, GameSettings gameSettings, CollisionCheck collisionCheck) {
        this.entityManagement = entityManagement;
        this.keyInputHandler = keyInputHandler;
        this.gameSettings = gameSettings;
        this.collisionCheck = collisionCheck;
        this.entityManagement.setCollisonCheck(collisionCheck);
        this.character = entityManagement.getCharacter();
        this.entityManagement.createProjectileManagement();
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
        entityManagement.updateEntities(lastFrame, collisionCheck);
        updateCharacterMovement(lastFrame);
        entityManagement.getProjectileManagement().updateProjectiles(lastFrame);
    }

    public void render() {
        entityManagement.renderEntities();
        entityManagement.renderCharacter();
        entityManagement.getCharacter().updateHitboxPosition();
    }

    private void updateCharacterMovement(double deltaTime) {
        Set<KeyCode> pressedKeys = keyInputHandler.getPressedKeys();
        double distance = character.getCharacterSpeed() * deltaTime;

        if (entityManagement.getCharacter() != null) {
            entityManagement.getCharacter().updateDirectionQueue(pressedKeys, directionQueue);

            if (!directionQueue.isEmpty()) {
                String newDirection = directionQueue.getLast();
                if (!newDirection.equals(entityManagement.getCharacter().getDirection())) {
                    entityManagement.getCharacter().setDirection(newDirection);
                }
            }

            if (pressedKeys.contains(gameSettings.getKeyMap().get("upKey"))) {
                moveCharacter(0, -distance);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("downKey"))) {
                moveCharacter(0, distance);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("leftKey"))) {
                moveCharacter(-distance, 0);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("rightKey"))) {
                moveCharacter(distance, 0);
            }
            long currentTime = System.currentTimeMillis();
            if (pressedKeys.contains(gameSettings.getKeyMap().get("shootKey"))) {
                character.setShooting(true);
                if (character.isShooting() && (currentTime - character.getLastShotTime() >= character.getShootCooldown())) {
                    entityManagement.getProjectileManagement().characterProjectile();
                    character.setLastShotTime(currentTime);
                }
            } else {
                character.setShooting(false);
            }
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
}