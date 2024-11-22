package org.projectgame.project2dgame.Controller;

import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.TileManagement.Tile;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

public class CollisionCheck {
    private final TileMap tileMap;
    private final EntityManagement entityManagement;

    public CollisionCheck(TileMap tileMap, EntityManagement entityManagement) {
        this.tileMap = tileMap;
        this.entityManagement = entityManagement;
    }

    public boolean checkCollision(Rectangle playerHitbox) {

        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if(tile.getHitbox() == null) {
                    continue;
                }
                if (playerHitbox.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    System.out.println("Kollision erkannt mit Tile bei (" + x + ", " + y + ")");
                    return true;
                }
            }
        }
        return false;
    }
}
