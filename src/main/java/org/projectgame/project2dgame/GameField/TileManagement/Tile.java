package org.projectgame.project2dgame.GameField.TileManagement;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.rgb;

public class Tile {
    private final Image image;
    private final int type;
    private Rectangle hitbox;

    public Tile(Image image, int type, int tileSize) {
        this.image = image;
        this.type = type;
        if (type != 1) {
            hitbox = new Rectangle(tileSize, tileSize);
            hitbox.setFill(rgb(255, 255, 255, 1));
            hitbox.setStroke(null);
        }
    }

    public Image getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}