package org.projectgame.project2dgame.Controller;

import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.Entity;
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
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCollisionEntity(Rectangle entityHitbox, double dx, double dy) {
        // Kollision mit Wänden prüfen
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if (tile.getHitbox() == null) {
                    continue;
                }

                Rectangle newHitbox = new Rectangle(
                        entityHitbox.getX() + dx,
                        entityHitbox.getY() + dy,
                        entityHitbox.getWidth(),
                        entityHitbox.getHeight()
                );
                if (newHitbox.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    return true;
                }
            }
        }

        // Kollision mit dem Spieler
        Character player = entityManagement.getCharacter();
        if (player != null) {
            Rectangle newHitbox = new Rectangle(
                    entityHitbox.getX() + dx,
                    entityHitbox.getY() + dy,
                    entityHitbox.getWidth(),
                    entityHitbox.getHeight()
            );
            if (newHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
                return true;
            }
        }

        // Kollision mit anderen Gegnern
        for (Entity entity : entityManagement.getEntity()) {
            if (entity.getHitbox() != entityHitbox) {
                Rectangle newHitbox = new Rectangle(
                        entityHitbox.getX() + dx,
                        entityHitbox.getY() + dy,
                        entityHitbox.getWidth(),
                        entityHitbox.getHeight()
                );
                if (newHitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                    return true;
                }
            }
        }

        return false;
    }


}
