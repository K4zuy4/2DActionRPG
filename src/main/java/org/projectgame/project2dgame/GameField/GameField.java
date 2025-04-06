package org.projectgame.project2dgame.GameField;

public class GameField {

    /// Konstanten für die Spielfeldgröße
    /// Die Größe des Spielfelds wird in Pixeln angegeben.
    /// Die Standardgröße ist 16x12 Felder mit einer Größe von 64x64 Pixeln pro Feld.

    public static final int ORIGINALE_TILE_GROESSE = 16; // 16x16 Pixel
    public static final int SCALE = 4;

    public static final int TILE_SIZE = ORIGINALE_TILE_GROESSE * SCALE;  // 64x64 Pixel

    public static final int MAX_TILES_X = 16;
    public static final int MAX_TILES_Y = 12;

    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_TILES_X;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_TILES_Y;

    public static final int LEVEL_COUNT = 5;

    // Aktiviert unendlich HP und zeigt Hitboxen
    public static final boolean DEBUG = false;

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static int getLevelCount() {
        return LEVEL_COUNT;
    }
}