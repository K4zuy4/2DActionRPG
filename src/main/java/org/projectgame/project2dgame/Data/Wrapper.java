package org.projectgame.project2dgame.Data;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class Wrapper {
    private Map<String, KeyCode> keyMap;

    public Wrapper() {
        this.keyMap = new HashMap<>();
        this.keyMap.put("upKey", KeyCode.W);
        this.keyMap.put("downKey", KeyCode.S);
        this.keyMap.put("leftKey", KeyCode.A);
        this.keyMap.put("rightKey", KeyCode.D);
    }

    public Wrapper(Map<String, KeyCode> keyMap) {
        this.keyMap = keyMap;
    }

    public Map<String, KeyCode> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, KeyCode> keyMap) {
        this.keyMap = keyMap;
    }
}
