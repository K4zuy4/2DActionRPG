package org.projectgame.project2dgame.Controller;

import com.almasb.fxgl.audio.Sound;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.TileManagement.Tile;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.util.List;

import static javafx.scene.paint.Color.rgb;

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
        // Kollision mit Wänden
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

        // Kollision mit Spieler
        Character player = entityManagement.getCharacter();
        if (player != null) {
            Rectangle newHitbox = new Rectangle(
                    entityHitbox.getX() + dx,
                    entityHitbox.getY() + dy,
                    entityHitbox.getWidth(),
                    entityHitbox.getHeight()
            );

            if (newHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
                player.takeDamage(15);
                return true;
            }
        }

        // Kollision mit Gegnern
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

    public boolean kannSpawnen(double spawnX, double spawnY, List<Entity> tempEntities) {
        double spriteSize = entityManagement.getGameField().getTileSize() * 1.5;
        double hitboxSize = entityManagement.getGameField().getTileSize() * 0.9;

        double adjustedX = spawnX + (spriteSize - hitboxSize) / 2;
        double adjustedY = spawnY + (spriteSize - hitboxSize);

        Rectangle tempHitbox = new Rectangle(adjustedX, adjustedY, hitboxSize + 20, hitboxSize + 20);

        // Kollision mit Wänden
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if (tile.getHitbox() != null && tempHitbox.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    //System.out.println("Kollision mit Wand"); debug
                    return false;
                }
            }
        }

        // Kollision mit Spieler
        Character player = entityManagement.getCharacter();
        if (player != null && tempHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            //System.out.println("Kollision mit Spieler"); debug
            return false;
        }

        // Kollision mit existierenden Gegnern
        for (Entity entity : entityManagement.getEntity()) {
            if (tempHitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                //System.out.println("Kollision mit Gegner (bestehende Entity)"); debug
                return false;
            }
        }

        // Kollision mit neuen Gegnern (Doppelter Check)
        for (Entity entity : tempEntities) {
            if (tempHitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                //System.out.println("Kollision mit Gegner (temp Entity)"); debug
                return false;
            }
        }

        return true;
    }

    public boolean checkCollisionProjectile(Rectangle projectileHitbox) {
        // Kollision mit Wänden
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if (tile.getHitbox() != null && projectileHitbox.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    return true;
                }
            }
        }

        // Kollision mit Gegnern
        for (Entity entity : entityManagement.getEntity()) {
            if (projectileHitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                entity.takeDamage(CharacterInfo.getDamage());
                SoundEngine.playEnemyHitSound();
                return true;
            }
        }

        return false;
    }


    // Mit Hilfe von ChatGPT damit der Pfeil selber als Hitbox zählt und keine Collision Bugs auftreten
    public boolean checkEnemyProjectileCollision(Node hitboxNode) {
        Character player = entityManagement.getCharacter();
        if (player == null) return false;

        // Spieler-Kollision
        if (hitboxNode.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            player.takeDamage(10);
            return true;
        }

        // Wand-Kollision
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if (tile.getHitbox() != null && hitboxNode.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    return true;
                }
            }
        }

        return false;
    }



    public boolean isObstacleBetween(double startX, double startY, double endX, double endY) {
        double dx = endX - startX;
        double dy = endY - startY;
        double steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) return false;

        double stepX = dx / steps;
        double stepY = dy / steps;
        double x = startX;
        double y = startY;

        for (int i = 0; i < steps; i++) {
            x += stepX;
            y += stepY;

            if (checkCollision(new Rectangle(x, y + 20, 1, 1))) {
                return true;
            }
        }
        return false;
    }
}