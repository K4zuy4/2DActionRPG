package org.projectgame.project2dgame.Controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyInputHandler {
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    public KeyInputHandler() {
    }

    public void addKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
    }

    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }
}