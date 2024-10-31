package org.projectgame.project2dgame.GameField;

public class GameField {

    final int originaleTileGroesse = 16; //16x16 Pixel
    final int scale = 3;

    final int tileSize = originaleTileGroesse * scale;  //48x48 Pixel

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
}
