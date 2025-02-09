package org.projectgame.project2dgame.GameField;

public class GameField {
    public int level = 0;

    public final int originaleTileGroesse = 16; //16x16 Pixel
    final int scale = 4;

    final int tileSize = originaleTileGroesse * scale;  //64x64 Pixel

    final int maxTilesX = 16;
    final int maxTilesY = 12;

    final int screenHeight = tileSize * maxTilesY;
    final int screenWidth = tileSize * maxTilesX;

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
