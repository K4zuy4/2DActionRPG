package org.projectgame.project2dgame.Entities;

import javafx.scene.layout.Pane;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.util.Random;

public class EntityManagement {
    private final Pane gamePane;
    private Character character;
    private Entity entity;
    private final GameField gameField;

    public EntityManagement(Pane gamePane, GameField gameField) {
        this.gamePane = gamePane;
        this.gameField = gameField;
    }

    public void loadEntities() {
        Random random = new Random();
        for(int i = 0; i < 10; i++) {
            int x = 64 + random.nextInt(800);
            int y = 64 + random.nextInt(550);

            entity = new Entity(x, y, 100, "/Entities/player_idle.gif", this.gameField);
            gamePane.getChildren().add(entity.getSprite());
            gamePane.getChildren().add(entity.getHitbox());
        }
    }

    public void loadCharacter() {
        character = new Character(100, 100, 100, "/Entities/player.png", this.gameField);
        gamePane.getChildren().add(character.getSprite());
        gamePane.getChildren().add(character.getHitbox());
    }

    public void updateEntities(double deltaTime) {
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
}

