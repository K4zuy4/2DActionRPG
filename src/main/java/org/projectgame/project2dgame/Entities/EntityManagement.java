package org.projectgame.project2dgame.Entities;

import javafx.scene.layout.Pane;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

public class EntityManagement {
    private final Pane gamePane;
    private final TileMap tileMap;
    private Character character;
    private final GameField gameField;

    public EntityManagement(Pane gamePane, TileMap tileMap, GameField gameField) {
        this.gamePane = gamePane;
        this.tileMap = tileMap;
        this.gameField = gameField;
    }

    public void loadEntities() {
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

