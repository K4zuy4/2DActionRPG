package org.projectgame.project2dgame.Entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Entities.Enemies.Slime;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityManagement {
    private final Pane gamePane;
    private Character character;
    private final GameField gameField;
    private final List<Entity> entities = new ArrayList<>();
    private ProjectileManagement projectileManagement;
    private CollisionCheck collisonCheck;

    public EntityManagement(Pane gamePane, GameField gameField) {
        this.gamePane = gamePane;
        this.gameField = gameField;
    }

    // Überarbeitet von ChatGPT, da Problem mit den Gegner, welche in einander spawnen durch zu kleine Verzögerung zwischen den Spawns
    public void loadEntities(CollisionCheck collisionCheck) {
        Random random = new Random();
        List<Entity> tempEntities = new ArrayList<>();

        Timeline timeline = new Timeline();

        for (int i = 0; i < 5; i++) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 30), event -> {
                int x, y;
                do {
                    x = 64 + random.nextInt(800);
                    y = 64 + random.nextInt(550);
                } while (!collisionCheck.kannSpawnen(x, y, tempEntities));

                if (collisionCheck.kannSpawnen(x, y, tempEntities)) {
                    Slime entity = new Slime(x, y, gameField, gamePane, this);
                    tempEntities.add(entity);
                    entities.add(entity);
                    gamePane.getChildren().addAll(entity.getSprite(), entity.getHitbox(), entity.getHealthBar());
                }
            }));
        }
        timeline.play();
    }

    public void loadCharacter() {
        character = new Character(100, 100, 100, "/Entities/player.png", this.gameField);
        gamePane.getChildren().add(character.getSprite());
        gamePane.getChildren().add(character.getHitbox());
        gamePane.getChildren().add(character.getHealthBar());
    }


    // Hilfe von Youtube Tutorials und ChatGPT
    public void updateEntities(double deltaTime, CollisionCheck collisionCheck) {
        for(Entity entity : entities) {
            entity.updateHitboxPosition();
        }

        Character player = getCharacter();
        if (player == null) return;

        double playerX = player.getX();
        double playerY = player.getY();

        for (Entity entity : entities) {

            if (!isPlayerVisible(entity, player, collisionCheck)) {
                zufaelligBewegen(entity, deltaTime, collisionCheck);
                continue;
            }
            entity.setIdle(false);

            double dx = playerX - entity.getX();
            double dy = playerY - entity.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                dx = (dx / distance) * entity.getEntitySpeed() * deltaTime;
                dy = (dy / distance) * entity.getEntitySpeed() * deltaTime;
            }

            double newX = entity.getX();
            double newY = entity.getY();

            if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), dx, 0)) {
                newX += dx;
            }

            if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), 0, dy)) {
                newY += dy;
            }

            entity.setX(newX);
            entity.setY(newY);
        }
    }


    // Slimes gehen nicht Idle
    private void zufaelligBewegen(Entity entity, double deltaTime, CollisionCheck collisionCheck) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - entity.lastIdleTime > entity.getDurationIdle()) {
            entity.setIdle(true);
            entity.lastIdleTime = currentTime;
            entity.setDurationIdle(2000 + (long) (Math.random() * 2000));
            return;
        }
        entity.setIdle(false);
        entity.setDurationIdle(1000 + (long) (Math.random() * 2000));

        if (currentTime - entity.getLastRandomMoveTime() > 2000 + Math.random() * 1000) {
            double angle = Math.random() * 2 * Math.PI;
            entity.setRandomDirectionX(Math.cos(angle));
            entity.setRandomDirectionY(Math.sin(angle));
            entity.setLastRandomMoveTime(currentTime);
        }

        double dx = entity.getRandomDirectionX() * entity.getEntitySpeed() * deltaTime;
        double dy = entity.getRandomDirectionY() * entity.getEntitySpeed() * deltaTime;

        double newX = entity.getX();
        double newY = entity.getY();

        if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), dx, 0)) {
            newX += dx;
        }

        if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), 0, dy)) {
            newY += dy;
        }

        entity.setX(newX);
        entity.setY(newY);
    }


    private boolean isPlayerVisible(Entity entity, Character player, CollisionCheck collisionCheck) {
        double startX = entity.getX() + entity.getSprite().getBoundsInLocal().getWidth() / 2;
        double startY = entity.getY() + entity.getSprite().getBoundsInLocal().getHeight() / 2;
        double endX = player.getX() + player.getSprite().getBoundsInLocal().getWidth() / 2;
        double endY = player.getY() + player.getSprite().getBoundsInLocal().getHeight() / 2;

        return !collisionCheck.isObstacleBetween(startX, startY, endX, endY);
    }


    public void renderEntities() {

    }

    public void renderCharacter() {
        if (character != null) {
            character.render();
        }
    }

    public GameField getGameField() { return gameField; }

    public Character getCharacter() {
        return character;
    }

    public List<Entity> getEntity() {
        return entities;
    }

    public void setCollisonCheck(CollisionCheck collisonCheck) {
        this.collisonCheck = collisonCheck;
    }

    public void createProjectileManagement() {
        this.projectileManagement = new ProjectileManagement(collisonCheck, this);
    }

    public ProjectileManagement getProjectileManagement() {
        return projectileManagement;
    }

    public void addProjectileToPane(Projectile projectile) {
        gamePane.getChildren().add(projectile.getSprite());
    }

    public void removeProjectileFromPane(Projectile projectile) {
        gamePane.getChildren().remove(projectile.getSprite());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        if (entity instanceof Slime) {
            gamePane.getChildren().remove(entity.getSprite());
            gamePane.getChildren().remove(entity.getHitbox());
            gamePane.getChildren().remove(entity.getHealthBar());
        }

        if(entities.isEmpty()) {
            try {
                Main.setWindow("Win", 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

