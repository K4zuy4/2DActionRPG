package org.projectgame.project2dgame.GameField.TileManagement;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Main;

import java.io.*;
import java.util.*;

import static javafx.scene.paint.Color.rgb;

public class TileMap {
    private final Tile[][] tiles;
    private final int tileSize;
    private final int width;
    private final int height;
    private final Pane tilePane;
    Map<Integer, Image> tileImageCache = new HashMap<>();

    public TileMap(String path, int tileSize, Pane _gamePane) throws IOException {
        this.tileSize = tileSize;
        this.tilePane = _gamePane;
        this.tiles = loadMap(path);
        this.width = tiles[0].length;
        this.height = tiles.length;
        drawTiles();
    }

    private Tile[][] loadMap(String path) throws IOException {
        InputStream inputStream = Main.class.getResourceAsStream(path);

        if (inputStream == null) {
            throw new IOException("File not found: " + path);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        int rows = 0;
        List<Tile[]> tileList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            Tile[] tileRow = new Tile[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                long type = Long.parseLong(tokens[i]);
                Image image = loadTileImage((int) type);
                tileRow[i] = new Tile(image, (int) type, tileSize);
            }
            tileList.add(tileRow);
            rows++;
        }

        return tileList.toArray(new Tile[rows][]);
    }

    private void drawTiles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = tiles[y][x];
                ImageView imageView = new ImageView(tile.getImage());
                imageView.setFitWidth(tileSize);
                imageView.setFitHeight(tileSize);
                imageView.setX(x * tileSize);
                imageView.setY(y * tileSize);

                tilePane.getChildren().add(imageView);
                if (tile.isSolid()) {
                    Rectangle hitbox = tile.getHitbox();
                    hitbox.setX(x * tileSize);
                    hitbox.setY(y * tileSize);

                    tilePane.getChildren().addAll(hitbox);
                } else if (tile.doesDamage()) {
                    Rectangle damageHitbox = tile.getDamageHitbox();
                    damageHitbox.setX(x * tileSize);
                    damageHitbox.setY(y * tileSize);

                    tilePane.getChildren().addAll(damageHitbox);
                }

            }
        }
        Main.getGeldLabel().toFront();
        Main.getImageView().toFront();
        Main.getTimeLabel().toFront();
        Main.getPausePane().toFront();
    }

    private Image loadTileImage(int type) throws IOException {
        if (tileImageCache.containsKey(type)) { // cache
            return tileImageCache.get(type);
        }

        String path = "/Tiles/Tiles/tile" + type + ".png";
        InputStream inputStream = Main.class.getResourceAsStream(path);

        if (inputStream == null) {
            throw new IOException("File not found: " + path);
        }

        Image image = new Image(inputStream);
        tileImageCache.put(type, image);
        return image;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Tile getTile(int y, int x) {
        return tiles[y][x];
    }

}
