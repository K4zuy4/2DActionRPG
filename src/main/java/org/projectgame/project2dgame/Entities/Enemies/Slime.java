package org.projectgame.project2dgame.Entities.Enemies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Objects;
import static javafx.scene.paint.Color.rgb;

public class Slime extends Entity {
    private final ImageView idleGif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private final ImageView currentSprite;

    public Slime(double x, double y, int health, GameField gameField, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 150, gameField, gamePane, entityManagement);

        idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-idle.gif"))));
        rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-right.gif"))));
        leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-left.gif"))));

        this.sprite = idleGif;
        this.currentSprite = idleGif;
        this.sprite.setFitWidth(gameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(gameField.getTileSize() * 1.7);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, gameField.getTileSize() * 0.7, gameField.getTileSize() * 0.7);
        this.hitbox.setFill(rgb(255, 255, 255, 0));

        updateHitboxPosition();
    }

    @Override
    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight() - 10));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y + 20);
    }

    @Override
    public void update(double deltaTime) {
        double movement = (Math.random() - 0.5) * entitySpeed * deltaTime;
        setX(getX() + movement);
    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
        if (!isIdle) {
            if (dx > 0) {
                if (currentSprite != rightGif) {
                    this.sprite.setImage(rightGif.getImage());
                }
            } else if (dx < 0) {
                if (currentSprite != leftGif) {
                    this.sprite.setImage(leftGif.getImage());
                }
            } else {
                if (currentSprite != idleGif) {
                    this.sprite.setImage(idleGif.getImage());
                }
            }
        } else {
            if (currentSprite != idleGif) {
                this.sprite.setImage(idleGif.getImage());
            }
        }
    }
}
