package org.projectgame.project2dgame.GameField.TileManagement;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Main;

import java.io.*;
import java.util.*;

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
                if (tile.getType() == 16 || tile.getType() == 17 || tile.getType() == 18 || tile.getType() == 33 || tile.getType() == 31 || tile.getType() == 46 || tile.getType() == 47 || tile.getType() == 48 || tile.getType() == 76 || tile.getType() == 77 || tile.getType() == 78 || tile.getType() == 85 || tile.getType() == 91 || tile.getType() == 93 || tile.getType() == 106 || tile.getType() == 107 || tile.getType() == 108 || tile.getType() == 145 || tile.getType() == 160 || tile.getType() == 231 || tile.getType() == 241 || tile.getType() == 251 || tile.getType() == 261 || tile.getType() == 351 || tile.getType() == 361 || tile.getType() == 381 || tile.getType() == 391 || tile.getType() == 401 || tile.getType() == 411 || tile.getType() == 501 || tile.getType() == 511 || tile.getType() == 531 || tile.getType() == 551 || tile.getType() == 561 || tile.getType() == 701 || tile.getType() == 711 || tile.getType() == 841) {
                    Rectangle rectangle = tile.getHitbox();
                    rectangle.setX(x * tileSize);
                    rectangle.setY(y * tileSize);

                    tilePane.getChildren().addAll(rectangle);
                }

            }
        }

        Main.getGeldLabel().toFront();
        Main.getImageView().toFront();
        Main.getTimeLabel().toFront();
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
