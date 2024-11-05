package org.projectgame.project2dgame.GameField;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.EntityManagement;

import java.util.Set;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private int movementSpeed;
    private final EntityManagement entityManagement;
    private final KeyInputHandler keyInputHandler;
    private GameSettings gameSettings;
    private boolean FPSAnzeigen = false;

    public GameLoop(EntityManagement entityManagement, KeyInputHandler keyInputHandler, GameSettings gameSettings) {
        this.entityManagement = entityManagement;
        this.keyInputHandler = keyInputHandler;
        this.gameSettings = gameSettings;
        this.movementSpeed = entityManagement.getCharacter().getCharacterSpeed();
    }

    @Override
    public void handle(long now) {
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
        entityManagement.updateEntities(lastFrame);
        updateCharacterMovement(lastFrame);
    }

    public void render() {
        entityManagement.renderEntities();
        entityManagement.renderCharacter();
        entityManagement.getCharacter().updateHitboxPosition();
    }

    private void updateCharacterMovement(double deltaTime) {
        Set<KeyCode> pressedKeys = keyInputHandler.getPressedKeys();
        double distance = movementSpeed * deltaTime;

        if (entityManagement.getCharacter() != null) {
            if (pressedKeys.contains(gameSettings.getKeyMap().get("upKey"))) {
                entityManagement.getCharacter().getMovementHandler().moveUp(distance);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("downKey"))) {
                entityManagement.getCharacter().getMovementHandler().moveDown(distance);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("leftKey"))) {
                entityManagement.getCharacter().getMovementHandler().moveLeft(distance);
            }
            if (pressedKeys.contains(gameSettings.getKeyMap().get("rightKey"))) {
                entityManagement.getCharacter().getMovementHandler().moveRight(distance);
            }
        }
    }
}
