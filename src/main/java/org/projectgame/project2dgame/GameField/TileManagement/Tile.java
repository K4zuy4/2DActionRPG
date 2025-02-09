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
        if (type == 16 || type == 17 || type == 18 || type == 33 || type == 31 || type == 46 || type == 47 || type == 48 || type == 76 || type == 77 || type == 78 || type == 85 || type == 91 || type == 93 || type == 106 || type == 107 || type == 108 || type == 145 || type == 160 || type == 231 || type == 241 || type == 251 || type == 261 || type == 351 || type == 361 || type == 381 || type == 391 || type == 401 || type == 411 || type == 501 || type == 511 || type == 531 || type == 551 || type == 561 || type == 701 || type == 711 || type == 841) {
            hitbox = new Rectangle(tileSize, tileSize);
            hitbox.setFill(rgb(255, 255, 255, 0));
            //hitbox.setStroke(null);
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