package org.projectgame.project2dgame.Controller;

import com.almasb.fxgl.audio.Sound;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Entities.Enemies.DeathBoss;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.TileManagement.Tile;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.util.List;

public class CollisionCheck {
    private final TileMap tileMap;
    private final EntityManagement entityManagement;

    public CollisionCheck(TileMap tileMap, EntityManagement entityManagement) {
        this.tileMap = tileMap;
        this.entityManagement = entityManagement;
    }

    public boolean checkCollision(Rectangle playerHitbox) {
        // Prüft, ob die Spieler-Hitbox mit einer Wand kollidiert

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

    public void checkDamageTiles(Rectangle playerHitbox) {
        // Prüft, ob der Spieler auf einem Schaden verursachenden Tile steht

        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                Rectangle damageBox = tile.getDamageHitbox();
                if (damageBox != null && playerHitbox.getBoundsInParent().intersects(damageBox.getBoundsInParent())) {
                    entityManagement.getCharacter().takeDamage(15, false);
                    return;
                }
            }
        }
    }


    public boolean checkBatCollisionWithEntity(Rectangle hitbox, Entity self, double dx, double dy) {
        // Spezielle Kollisionserkennung für Bats, damit sie nicht in andere Entities buggen
        Rectangle movedHitbox = new Rectangle(
                hitbox.getX() + dx,
                hitbox.getY() + dy,
                hitbox.getWidth(),
                hitbox.getHeight()
        );

        for (Entity entity : entityManagement.getEntity()) {
            if (entity != self && movedHitbox.getBoundsInLocal().intersects(entity.getHitbox().getBoundsInLocal())) {
                return true;
            }
        }

        return false;
    }


    public boolean checkCollisionEntity(Rectangle entityHitbox, double dx, double dy) {
        // Prüft Entity-Kollisionen mit Wänden, dem Spieler oder anderen Entities

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
                player.takeDamage(15, false);
                return true;
            }
        }

        // Kollision mit Gegnern
        for (Entity entity : entityManagement.getEntity()) {
            if(entity instanceof DeathBoss) continue;
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

    public boolean kannSpawnen(Rectangle hitbox, List<Entity> tempEntities) {
        // Prüft, ob an einer Position ein Entity gespawnt werden kann (ohne Kollisionen)


        // Kollision mit Wänden
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(y, x);
                if (tile.getHitbox() != null &&
                        hitbox.getBoundsInParent().intersects(tile.getHitbox().getBoundsInParent())) {
                    return false;
                }
            }
        }

        // Kollision mit Spieler
        Character player = entityManagement.getCharacter();
        if (hitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            return false;
        }

        // Kollision mit bereits gespawnten Gegnern
        for (Entity entity : entityManagement.getEntity()) {
            if (hitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                return false;
            }
        }

        // Kollision mit temporären Gegnern
        for (Entity entity : tempEntities) {
            if (hitbox.getBoundsInParent().intersects(entity.getHitbox().getBoundsInParent())) {
                return false;
            }
        }

        return true;
    }


    public boolean checkCollisionProjectile(Rectangle projectileHitbox) {
        // Prüft, ob ein Projektil eine Wand oder einen Gegner trifft

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
        // Prüft, ob ein gegnerisches Projektil den Spieler oder eine Wand trifft

        Character player = entityManagement.getCharacter();
        if (player == null) return false;

        // Spieler-Kollision
        if (hitboxNode.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            player.takeDamage(10, false);
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
        // Prüft, ob eine Wand zwischen zwei Punkten liegt (für Line-Of-Sight Checks)

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