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
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.util.Set;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private int movementSpeed;
    private TileMap tileMap;
    private final EntityManagement entityManagement;
    private final KeyInputHandler keyInputHandler;
    private final CollisionCheck collisionCheck;
    private GameSettings gameSettings;
    private boolean FPSAnzeigen = false;
    private boolean running = true;

    public GameLoop(EntityManagement entityManagement, KeyInputHandler keyInputHandler, GameSettings gameSettings, TileMap tileMap) {
        this.entityManagement = entityManagement;
        this.keyInputHandler = keyInputHandler;
        this.gameSettings = gameSettings;
        this.movementSpeed = entityManagement.getCharacter().getCharacterSpeed();
        this.tileMap = tileMap;
        this.collisionCheck = new CollisionCheck(this.tileMap, this.entityManagement);
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

    public void stopLoop() {
        running = false;
    }

    public void update(double lastFrame) {
        entityManagement.updateEntities(lastFrame, collisionCheck);
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
        }
    }

    private void moveCharacter(double dx, double dy) {
        Character character = entityManagement.getCharacter();
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