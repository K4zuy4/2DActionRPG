package org.projectgame.project2dgame.Entities;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Entities.Enemies.Skeleton;
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
    private final Label geldLabel;
    private final int level;
    private long lastMoveTime = System.currentTimeMillis();

    public EntityManagement(Pane gamePane, GameField gameField, Label geldLabel, int level) {
        this.gamePane = gamePane;
        this.gameField = gameField;
        this.geldLabel = geldLabel;
        this.level = level;
    }

    // Überarbeitet von ChatGPT, da Problem mit den Gegner, welche in einander spawnen durch zu kleine Verzögerung zwischen den Spawns
    public void loadEntities(CollisionCheck collisionCheck) {
        int slamount;
        int skamount = 0;
        int slimeHealth;
        int skeletonHealth;

        if(level == 1) {
            skeletonHealth = 0;
            slamount = 5;
            slimeHealth = 50;
        } else if(level == 2) {
            slamount = 5;
            skamount = 3;
            slimeHealth = 60;
            skeletonHealth = 100;
        } else if(level == 3) {
            slamount = 6;
            skamount = 4;
            slimeHealth = 70;
            skeletonHealth = 120;
        } else {
            skeletonHealth = 0;
            slimeHealth = 0;
            slamount = 0;
            skamount = 0;
        }

        int totalAmount = slamount + skamount;
        Random random = new Random();
        List<Entity> tempEntities = new ArrayList<>();
        Timeline timeline = new Timeline();

        for (int i = 0; i < totalAmount; i++) {
            int finalI = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 150), event -> {
                int x, y;
                do {
                    x = 64 + random.nextInt(800);
                    y = 64 + random.nextInt(550);
                } while (!collisionCheck.kannSpawnen(x, y, tempEntities));

                if (collisionCheck.kannSpawnen(x, y, tempEntities)) {
                    if (finalI < slamount) {
                        Slime entity = new Slime(x, y, slimeHealth, gameField, gamePane, this);
                        tempEntities.add(entity);
                        entities.add(entity);
                        gamePane.getChildren().addAll(entity.getSprite(), entity.getHitbox(), entity.getHealthBar());
                    } else {
                        Skeleton entity = new Skeleton(x, y, skeletonHealth, gameField, gamePane, this);
                        tempEntities.add(entity);
                        entities.add(entity);
                        gamePane.getChildren().addAll(entity.getSprite(), entity.getHitbox(), entity.getHealthBar());
                    }
                }
            }));
        }

        timeline.play();
    }

    public void loadCharacter() {
        character = new Character(100, 300, this.gameField, this);
        gamePane.getChildren().add(character.getSprite());
        gamePane.getChildren().add(character.getHitbox());
        gamePane.getChildren().add(character.getHealthBar());
    }

    // Hilfe von Youtube Tutorials und ChatGPT
    public void updateEntities(double deltaTime, CollisionCheck collisionCheck) {
        for (Entity entity : entities) {
            entity.updateHitboxPosition();
        }

        Character player = getCharacter();
        if (player == null) return;

        double playerX = player.getX();
        double playerY = player.getY();

        for (Entity entity : entities) {
            if (entity.isWaiting()) continue;

            // Slime
            if (entity instanceof Slime) {
                if (!isPlayerVisible(entity, player, collisionCheck)) {
                    zufaelligBewegen(entity, deltaTime, collisionCheck);
                    continue;
                }
                if (entity.getCurrentPause() != null) {
                    entity.getCurrentPause().stop();
                    entity.setCurrentPause(null);
                    entity.setWaiting(false);
                    entity.setIdle(false);
                }
                moveTowards(entity, playerX, playerY, deltaTime, collisionCheck);
            }

            // Skeleton
            if (entity instanceof Skeleton skeleton) {
                if (skeleton.isInCooldown()) {
                    if (System.currentTimeMillis() - skeleton.getCooldownStartTime() < skeleton.getCooldownDuration()) {
                        continue;
                    } else {
                        skeleton.endCooldown();
                    }
                }

                if (skeleton.isAttacking()) continue;

                if (skeleton.isRetreating()) {
                    if (System.currentTimeMillis() - skeleton.getRetreatStartTime() < skeleton.getRetreatDuration()) {
                        moveAwayFrom(skeleton, playerX, playerY, deltaTime, collisionCheck);
                        continue;
                    } else {
                        skeleton.resetRetreat();
                    }
                }

                if (!isPlayerVisible(entity, player, collisionCheck)) {
                    zufaelligBewegen(entity, deltaTime, collisionCheck);

                    continue;
                }

                if (skeleton.getCurrentPause() != null) {
                    skeleton.getCurrentPause().stop();
                    skeleton.setCurrentPause(null);
                    skeleton.setWaiting(false);
                    skeleton.setIdle(false);
                }

                double distance = Math.hypot(playerX - skeleton.getX(), playerY - skeleton.getY());

                if (distance < 400) {
                    if (skeleton.getShotsFired() < 2) {
                        skeleton.startAttack();
                        skeleton.incrementShotsFired();
                    } else {
                        skeleton.startRetreat();
                    }
                } else {
                    moveTowards(skeleton, playerX, playerY, deltaTime, collisionCheck);
                }
            }
        }
    }


    private void moveTowards(Entity entity, double targetX, double targetY, double deltaTime, CollisionCheck collisionCheck) {
       entity.setIdle(false);
        double dx = targetX - entity.getX();
        double dy = targetY - entity.getY();
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

    private void moveAwayFrom(Entity entity, double fromX, double fromY, double deltaTime, CollisionCheck collisionCheck) {
        entity.setIdle(false);
        double dx = entity.getX() - fromX;
        double dy = entity.getY() - fromY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            dx = (dx / distance) * entity.getEntitySpeed() * deltaTime;
            dy = (dy / distance) * entity.getEntitySpeed() * deltaTime;
        }

        double newX = entity.getX();
        double newY = entity.getY();

        if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), dx, 0)) newX += dx;
        if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), 0, dy)) newY += dy;

        entity.setX(newX);
        entity.setY(newY);
    }


    private void zufaelligBewegen(Entity entity, double deltaTime, CollisionCheck collisionCheck) {

        if (entity.isWaiting()) return;

        if (entity.getRandomDirectionX() == 0 && entity.getRandomDirectionY() == 0) {
            double angle = Math.random() * 2 * Math.PI;
            entity.setRandomDirectionX(Math.cos(angle));
            entity.setRandomDirectionY(Math.sin(angle));
        }

        double dx = entity.getRandomDirectionX() * entity.getEntitySpeed() * deltaTime;
        double dy = entity.getRandomDirectionY() * entity.getEntitySpeed() * deltaTime;

        boolean collisionX = collisionCheck.checkCollisionEntity(entity.getHitbox(), dx, 0);
        boolean collisionY = collisionCheck.checkCollisionEntity(entity.getHitbox(), 0, dy);

        if (collisionX || collisionY) {
            entity.setIdle(true);
            entity.setWaiting(true);
            entity.updateSpriteDirection(0, 0);

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            entity.setCurrentPause(pause);
            pause.setOnFinished(e -> {
                entity.setWaiting(false);
                entity.setIdle(false);

                double angle = Math.random() * 2 * Math.PI;
                entity.setRandomDirectionX(Math.cos(angle));
                entity.setRandomDirectionY(Math.sin(angle));

                entity.setWaiting(false);
                entity.setIdle(false);
                entity.setCurrentPause(null);
            });


            pause.play();
            return;
        }

        double newX = entity.getX() + dx;
        double newY = entity.getY() + dy;

        entity.setX(newX);
        entity.setY(newY);

        entity.setIdle(false);
        entity.updateSpriteDirection(dx, dy);
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

    public void addEnemyProjectileToPane(EnemyProjectile projectile) {
        gamePane.getChildren().add(projectile.getSprite());
    }

    public void removeProjectileFromPane(Projectile projectile) {
        gamePane.getChildren().remove(projectile.getSprite());
    }

    public void removeEnemyProjectileFromPane(EnemyProjectile projectile) {
        gamePane.getChildren().remove(projectile.getSprite());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
            gamePane.getChildren().remove(entity.getSprite());
            gamePane.getChildren().remove(entity.getHitbox());
            gamePane.getChildren().remove(entity.getHealthBar());
            Random random = new Random();
            int geld = 0;
            if(entity instanceof Slime) {
                geld = random.nextInt(5 * 2 + 1) + (10 - 3);
                character.setGeld(character.getGeld() + geld);
            } else if(entity instanceof Skeleton) {
                geld = random.nextInt(5 * 2 + 1) + (15 - 3);
                character.setGeld(character.getGeld() + geld);
            }
            character.setGeld(character.getGeld() + geld);

        if(entities.isEmpty()) {
            try {
                Main.safeGameTime(level);
                if (!CharacterInfo.getLevelDone().contains(level)) {
                    CharacterInfo.getLevelDone().add(level);
                }
                Main.setWindow("Win", 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Label getGeldLabel() {
        return geldLabel;
    }
}

