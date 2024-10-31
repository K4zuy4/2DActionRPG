package org.projectgame.project2dgame.GameField.TileManagement;

import javafx.scene.image.Image;

public class Tile {
    private final Image image;
    private final int type;

    public Tile(Image image, int type) {
        this.image = image;
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public int getType() {
        return type;
    }
}
