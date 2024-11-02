package org.projectgame.project2dgame.GameField;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Entities.EntityManagement;

import java.util.Set;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private final EntityManagement entityManagement;
    private final KeyInputHandler keyInputHandler;

    public GameLoop(EntityManagement entityManagement, KeyInputHandler keyInputHandler) {
        this.entityManagement = entityManagement;
        this.keyInputHandler = keyInputHandler;
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

        if (now - lastFpsUpdate >= 1_000_000_000) { // Update FPS every second
            int fps = frameCount;
            System.out.println("FPS: " + fps);
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
    }

    private void updateCharacterMovement(double deltaTime) {
        Set<KeyCode> pressedKeys = keyInputHandler.getPressedKeys();
        double distance = 200 * deltaTime; //Geschwindigkeit in Pixel pro Sekunde

        if (entityManagement.getCharacter() != null) {
            if (pressedKeys.contains(KeyCode.W)) {
                entityManagement.getCharacter().getMovementHandler().moveUp(distance);
            }
            if (pressedKeys.contains(KeyCode.S)) {
                entityManagement.getCharacter().getMovementHandler().moveDown(distance);
            }
            if (pressedKeys.contains(KeyCode.A)) {
                entityManagement.getCharacter().getMovementHandler().moveLeft(distance);
            }
            if (pressedKeys.contains(KeyCode.D)) {
                entityManagement.getCharacter().getMovementHandler().moveRight(distance);
            }
        }
    }
}
