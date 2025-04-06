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
        // Registriert Key-Pressed und Key-Released Events auf der angegebenen Szene

        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent event) {
        // Fügt eine gedrückte Taste dem Set hinzu

        pressedKeys.add(event.getCode());
    }

    private void handleKeyReleased(KeyEvent event) {
        // Entfernt eine losgelassene Taste aus dem Set

        pressedKeys.remove(event.getCode());
    }

    public Set<KeyCode> getPressedKeys() {
        // Gibt das Set aller aktuell gedrückten Tasten zurück

        return pressedKeys;
    }
}