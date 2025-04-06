package org.projectgame.project2dgame.Entities.Enemies;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.util.Objects;
import static javafx.scene.paint.Color.rgb;

public class Slime extends Entity {
    // Animationen des Skeletts für verschiedene Zustände
    private final ImageView idleGif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private ImageView currentSprite;
    private final ImageView spawnGif;

    // Statusvariablen
    private boolean isIdle;

    public Slime(double x, double y, int health, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 150, gamePane, entityManagement);

        // Animationen laden
        idleGif = EntityManagement.getImage("slime-idle");
        rightGif = EntityManagement.getImage("slime-right");
        leftGif = EntityManagement.getImage("slime-left");
        spawnGif = EntityManagement.getImage("slime-spawn");

        this.sprite = spawnGif;
        this.currentSprite = spawnGif;

        // Nach Spawn-Delay in Idle wechseln
        PauseTransition spawnDelay = new PauseTransition(Duration.seconds(1.5));
        spawnDelay.setOnFinished(e -> {
            this.sprite.setImage(idleGif.getImage());
            this.currentSprite = idleGif;
        });
        spawnDelay.play();

        // Sprite und Hitbox konfigurieren
        this.sprite.setFitWidth(GameField.getTileSize() * 3);
        this.sprite.setFitHeight(GameField.getTileSize() * 3);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, GameField.getTileSize() * 0.8, GameField.getTileSize() * 0.8);
        if(GameField.isDebug()) {
            hitbox.setFill(rgb(0, 255, 0, 0.5));
        } else {
            hitbox.setFill(rgb(255, 0, 0, 0));
        }

        updateHitboxPosition();
    }

    @Override
    public void update(double deltaTime) {
        // Update-Logik wird in der EntityManagement behandelt
    }

    @Override
    public void updateHitboxPosition() {
        // Aktualisiert Hitbox-Position relativ zur Sprite-Position
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()) - 73);
        healthBar.setLayoutX(x + 60);
        healthBar.setLayoutY(y + 40);
    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
        // Wählt und setzt die passende Animation je nach Status und Bewegungsrichtung
        if (Main.getGameLoop() != null && !Main.isGameLoopPaused()) {
            this.sprite.setFitWidth(GameField.getTileSize() * 3);
            this.sprite.setFitHeight(GameField.getTileSize() * 3);
            if (!isIdle) {
                if (dx > 0) {
                    if (currentSprite != rightGif) {
                        setSprite(rightGif);
                    }
                } else if (dx < 0) {
                    if (currentSprite != leftGif) {
                        setSprite(leftGif);
                    }
                }
            } else {
                if (currentSprite != idleGif) {
                    setSprite(idleGif);
                }
            }
        }
    }

    private void setSprite(ImageView newSprite) {
        // Hilfsmethode zum Wechseln des aktuellen Sprites
        if (newSprite == currentSprite) return;

        newSprite.setX(x);
        newSprite.setY(y);
        this.sprite.setFitWidth(GameField.getTileSize() * 3);
        this.sprite.setFitHeight(GameField.getTileSize() * 3);

        if(sprite != null) gamePane.getChildren().remove(sprite);
        gamePane.getChildren().remove(currentSprite);
        gamePane.getChildren().add(newSprite);

        this.sprite = newSprite;
        this.currentSprite = newSprite;
        this.sprite.setFitWidth(GameField.getTileSize() * 3);
        this.sprite.setFitHeight(GameField.getTileSize() * 3);
    }

    @Override
    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public boolean isIdle() {
        return isIdle;
    }
}
