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
    private final ImageView idle2Gif;
    private final ImageView rightGif;
    private final ImageView leftGif;
    private ImageView currentSprite;
    private boolean isIdle;

    public Slime(double x, double y, int health, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, 150, gamePane, entityManagement);

        idleGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-idle.gif"))));
        rightGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-right.gif"))));
        leftGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-left.gif"))));
        idle2Gif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/Slime 1/slime1-idle2.gif"))));

        this.sprite = idleGif;
        this.currentSprite = idleGif;
        this.sprite.setFitWidth(GameField.getTileSize() * 1.8);
        this.sprite.setFitHeight(GameField.getTileSize() * 1.7);
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

    }

    @Override
    public void updateHitboxPosition() {
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight() - 10));
        healthBar.setLayoutX(x);
        healthBar.setLayoutY(y + 20);
    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
        this.sprite.setFitWidth(GameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(GameField.getTileSize() * 1.7);
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
            if (currentSprite != idle2Gif) {
                setSprite(idle2Gif);
            }
        }
    }

    private void setSprite(ImageView newSprite) {
        if (newSprite == currentSprite) return;

        newSprite.setX(x);
        newSprite.setY(y);
        this.sprite.setFitWidth(GameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(GameField.getTileSize() * 1.7);

        gamePane.getChildren().remove(currentSprite);
        gamePane.getChildren().add(newSprite);

        this.sprite = newSprite;
        this.currentSprite = newSprite;
        this.sprite.setFitWidth(GameField.getTileSize() * 1.7);
        this.sprite.setFitHeight(GameField.getTileSize() * 1.7);
    }

    @Override
    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public boolean isIdle() {
        return isIdle;
    }
}
