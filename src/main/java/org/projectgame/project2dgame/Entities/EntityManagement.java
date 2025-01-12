package org.projectgame.project2dgame.Entities;

import javafx.scene.layout.Pane;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityManagement {
    private final Pane gamePane;
    private Character character;
    private Entity entity;
    private final GameField gameField;
    private List<Entity> entities = new ArrayList<>();

    public EntityManagement(Pane gamePane, GameField gameField) {
        this.gamePane = gamePane;
        this.gameField = gameField;
    }

    public void loadEntities() {
        Random random = new Random();
        for(int i = 0; i < 5; i++) {
            int x = 64 + random.nextInt(800);
            int y = 64 + random.nextInt(550);

            entity = new Entity(x, y, 100, "/Entities/player_idle.gif", this.gameField);
            entities.add(entity);
            gamePane.getChildren().add(entity.getSprite());
            gamePane.getChildren().add(entity.getHitbox());
        }
    }

    public void loadCharacter() {
        character = new Character(100, 100, 100, "/Entities/player.png", this.gameField);
        gamePane.getChildren().add(character.getSprite());
        gamePane.getChildren().add(character.getHitbox());
    }

    public void updateEntities(double deltaTime, CollisionCheck collisionCheck) {
        for(Entity entity : entities) {
            entity.updateHitboxPosition();
        }

        Character player = getCharacter();
        if (player == null) return;

        double playerX = player.getX();
        double playerY = player.getY();

        for (Entity entity : entities) {
            double dx = playerX - entity.getX();
            double dy = playerY - entity.getY();

            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance > 0) {
                dx = (dx / distance) * entity.getEntitySpeed() * deltaTime;
                dy = (dy / distance) * entity.getEntitySpeed() * deltaTime;
            }

            double newX = entity.getX();
            double newY = entity.getY();

            // Prüfe horizontale Bewegung
            if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), dx, 0)) {
                newX += dx;
            }

            // Prüfe vertikale Bewegung
            if (!collisionCheck.checkCollisionEntity(entity.getHitbox(), 0, dy)) {
                newY += dy;
            }

            entity.setX(newX);
            entity.setY(newY);
        }

    }

    public void renderEntities() {

    }

    public void renderCharacter() {
        if (character != null) {
            character.render();
        }
    }

    public GameField getGameField() {
        return gameField;
    }

    public Character getCharacter() {
        return character;
    }

    public List<Entity> getEntity() {
        return entities;
    }
}

