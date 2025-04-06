/*
    * Wrapper zum Speichern der Zeiten für die Level
 */


package org.projectgame.project2dgame.Data;

public class TimeWrapper {
    private int level;
    private double time;
    private String date;

    public TimeWrapper() {}

    public TimeWrapper(int level, double time, String date) {
        this.level = level;
        this.time = time;
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public double getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Level " + level + ": " + time + " Sekunden (" + date + ")";
    }
}
