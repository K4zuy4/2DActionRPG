package org.projectgame.project2dgame.GameField.TileManagement;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.GameField.GameField;

import java.util.Set;

import static javafx.scene.paint.Color.rgb;

public class Tile {
    // Bassis Informationen
    private final Image image;
    private Rectangle hitbox;
    private Rectangle damageHitbox;
    private boolean isSolid = false;
    private boolean doDamage = false;

    // Set mit den Typen, die eine Hitbox haben
    private final Set<Integer> TYPES_WITH_HITBOX = Set.of(
            16, 17, 18, 31, 33, 46, 47, 48,
            76, 77, 78, 85, 91, 93,
            106, 107, 108, 145, 160,
            231, 241, 251, 261,
            351, 361, 381, 391, 401, 411,
            501, 511, 531, 551, 561,
            701, 711, 841
    );

    public Tile(Image image, int type, int tileSize) {
        // Konstruktor für die Tiles

        this.image = image;
        if (TYPES_WITH_HITBOX.contains(type)) {
            hitbox = new Rectangle(tileSize, tileSize);
            if(GameField.isDebug()) {
                hitbox.setFill(rgb(255, 255, 255, 0.5));
            } else {
                hitbox.setFill(rgb(255, 0, 0, 0));
            }
            isSolid = true;
        }

        if (type == 329) {
            damageHitbox = new Rectangle(tileSize, tileSize);
            if(GameField.isDebug()) {
                damageHitbox.setFill(rgb(255, 0, 0, 0.5));
            } else {
                damageHitbox.setFill(rgb(0, 0, 0, 0));
            }
            doDamage = true;
        }
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Rectangle getDamageHitbox() {
        return damageHitbox;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean doesDamage() {
        return doDamage;
    }
}