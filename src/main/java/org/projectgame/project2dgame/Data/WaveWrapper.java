package org.projectgame.project2dgame.Data;

public class WaveWrapper {
    private int waves;
    private double time;
    private String date;

    public WaveWrapper() {}

    public WaveWrapper(int waves, double time, String date) {
        this.waves = waves;
        this.time = time;
        this.date = date;
    }

    public int getWaves() {
        return waves;
    }

    public double getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setWaves(int waves) {
        this.waves = waves;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Wellen " + waves + ": " + time + " Sekunden (" + date + ")";
    }
}
