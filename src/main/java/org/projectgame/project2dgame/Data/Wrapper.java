package org.projectgame.project2dgame.Data;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class Wrapper {
    private Map<String, KeyCode> keyMap;
    private double volume;

    public Wrapper() {}

    public Wrapper(Map<String, KeyCode> keyMap, double volume) {
        this.keyMap = keyMap;
        this.volume = volume;
    }

    public Map<String, KeyCode> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, KeyCode> keyMap) {
        this.keyMap = keyMap;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
