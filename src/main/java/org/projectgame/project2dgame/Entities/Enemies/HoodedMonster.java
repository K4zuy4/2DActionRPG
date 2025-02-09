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

public class HoodedMonster extends Entity {

    public HoodedMonster(double x, double y, GameField gameField, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, 150, 120, gameField, gamePane, entityManagement);

        this.sprite = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Entities/hoodedMonster.png"))));
        this.sprite.setFitWidth(gameField.getTileSize() * 1.5);
        this.sprite.setFitHeight(gameField.getTileSize() * 2);
        this.sprite.setX(x);
        this.sprite.setY(y);

        this.hitbox = new Rectangle(x, y, gameField.getTileSize(), gameField.getTileSize());
        this.hitbox.setFill(rgb(0, 0, 255, 0.5));

        updateHitboxPosition();
    }

    @Override
    public void updateHitboxPosition() {

    }

    @Override
    public void updateSpriteDirection(double dx, double dy) {
    }

    @Override
    public void update(double deltaTime) {
    }
}
