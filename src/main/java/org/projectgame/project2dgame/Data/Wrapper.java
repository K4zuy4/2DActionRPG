package org.projectgame.project2dgame.Data;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Wrapper zum Speichern aller Daten
 */



public class Wrapper {
    private Map<String, KeyCode> keyMap;
    private double volume;
    private List<TimeWrapper> times;
    private List<WaveWrapper> waves;

    public Wrapper() {}

    public Wrapper(Map<String, KeyCode> keyMap, double volume, List<TimeWrapper> times, List<WaveWrapper> waves) {
        this.keyMap = keyMap;
        this.volume = volume;
        this.times = times;
        this.waves = waves;
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

    public List<TimeWrapper> getTimes() {
        return times;
    }

    public void setTimes(List<TimeWrapper> times) {
        this.times = times;
    }

    public List<WaveWrapper> getWaves() {
        return waves;
    }

    public void setWaves(List<WaveWrapper> waves) {
        this.waves = waves;
    }
}
