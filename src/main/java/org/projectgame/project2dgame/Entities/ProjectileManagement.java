package org.projectgame.project2dgame.Entities;

import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Controller.CollisionCheck;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.rgb;

public class ProjectileManagement {
    private final CollisionCheck collisionCheck;
    private final EntityManagement entityManagement;
    private final List<Projectile> playerProjectiles = new ArrayList<>();
    private List<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private final Character character;

    public ProjectileManagement(CollisionCheck collisionCheck, EntityManagement entityManagement) {
        this.collisionCheck = collisionCheck;
        this.entityManagement = entityManagement;
        this.character = entityManagement.getCharacter();
    }

    //Ebenso von ChatGPT überarbeitet, da die Projektile nicht richtig gespawnt wurden
    public void characterProjectile() {
        String direction = character.getDirection();

        double dirX = 0, dirY = 0;
        String spritePath = switch (direction) {
            case "up" -> {
                dirY = -1;
                yield "/VFX/FB_Up.gif";
            }
            case "down" -> {
                dirY = 1;
                yield "/VFX/FB_Down.gif";
            }
            case "left" -> {
                dirX = -1;
                yield "/VFX/FB_Left.gif";
            }
            case "right" -> {
                dirX = 1;
                yield "/VFX/FB_Right.gif";
            }
            default -> "";

            // https://nyknck.itch.io/pixelarteffectfx017 Fireball VFX
        };

        double hitboxWidth = character.getHitbox().getWidth();
        double hitboxHeight = character.getHitbox().getHeight();

        double projectileWidth = 64;
        double projectileHeight = 32;

        if (direction.equals("up") || direction.equals("down")) {
            projectileWidth = 32;
            projectileHeight = 64;
        }

        double projectileX = character.getHitbox().getX() + (hitboxWidth / 2) - (projectileWidth / 2);
        double projectileY = character.getHitbox().getY() + (hitboxHeight / 2) - (projectileHeight / 2);

        double spawnOffset = 20;
        projectileX += dirX * spawnOffset;
        projectileY += dirY * spawnOffset;

        if (collisionCheck.checkCollisionProjectile(new Rectangle(projectileX, projectileY, projectileWidth, projectileHeight))) {
            return;
        }

        Projectile projectile = new Projectile(
                projectileX, projectileY,
                dirX, dirY,
                spritePath, entityManagement.getGameField()
        );
        playerProjectiles.add(projectile);
        entityManagement.addProjectileToPane(projectile);
    }

    public void spawnEnemyArrow(double startX, double startY, double targetX, double targetY) {
        double dx = targetX + 25 - startX;
        double dy = targetY + 60 - startY;

        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;

        dx /= distance;
        dy /= distance;

        String spritePath = "/Entities/Skeleton/Arrow.png";


        double spawnOffset = 40;
        double projectileX = startX + dx * spawnOffset;
        double projectileY = startY + dy * spawnOffset;


        EnemyProjectile projectile = new EnemyProjectile(
                projectileX, projectileY,
                dx, dy,
                spritePath,
                400
        );

        enemyProjectiles.add(projectile);
        entityManagement.addEnemyProjectileToPane(projectile);
    }


    public void updateProjectiles(double deltaTime) {
        for (Projectile p : new ArrayList<>(playerProjectiles)) {
           p.update(deltaTime);

            if (collisionCheck.checkCollisionProjectile(p.getHitbox())) {
                p.deactivate();
            }

            if (!p.isActive()) {
                playerProjectiles.remove(p);
                entityManagement.removeProjectileFromPane(p);
            }
        }

        for (EnemyProjectile p : new ArrayList<>(enemyProjectiles)) {
            p.update(deltaTime);

            if (collisionCheck.checkEnemyProjectileCollision(p.getHitbox())) {
                p.deactivate();
            }

            if (!p.isActive()) {
                enemyProjectiles.remove(p);
                entityManagement.removeEnemyProjectileFromPane(p);
            }
        }


    }
}
