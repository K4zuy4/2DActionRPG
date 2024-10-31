package org.projectgame.project2dgame.GameField;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;

    @Override
    public void handle(long now) {
        if(lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double lastFrame = (now - lastUpdate) / 1_000_000_000.0; //Nanosekunde zu Sekunde

        update(lastFrame);

        render();

        lastUpdate = now;

    }

    public void update(double lastFrame) {

    }

    public void render() {

    }
}
