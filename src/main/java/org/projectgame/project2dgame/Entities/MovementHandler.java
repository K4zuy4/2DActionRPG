package org.projectgame.project2dgame.Entities;

public class MovementHandler {
    private final Character character;

    public MovementHandler(Character character) {
        this.character = character;
    }

    public void moveUp(double distance) {
        character.setY(character.getY() - distance);
    }

    public void moveDown(double distance) {
        character.setY(character.getY() + distance);
    }

    public void moveLeft(double distance) {
        character.setX(character.getX() - distance);
    }

    public void moveRight(double distance) {
        character.setX(character.getX() + distance);
    }
}