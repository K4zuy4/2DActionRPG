package org.projectgame.project2dgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Controller.GameFieldController;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Controller.SoundEngine;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.GameLoop;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static GameLoop gameLoop;
    private final static SoundEngine soundEngine = new SoundEngine();
    public static GameSettings gameSettings;
    private static final GameField gameField = new GameField();
    private static Label geldLabel;
    private static ImageView imageView;

    static {
        try {
            gameSettings = new GameSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException{
        primaryStage = stage;
        setWindow("MainMenu", 0);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        Debug debug = new Debug(gameSettings);

        primaryStage.show();
    }

    public static void setWindow(String window, int level) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        switch (window) {
            case "MainMenu":
                loader.setLocation(Main.class.getResource("/FXMLFiles/MainMenu.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Main Menu");
                soundEngine.playMainMenuMusic();
                break;

            case "GameField":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameField.fxml"));
                Parent root = loader.load();
                GameFieldController controller = loader.getController();
                Pane gamePane = controller.getGamePane();
                geldLabel = controller.getGeldLabel();
                imageView = controller.getImageView();
                gamePane.getChildren().add(geldLabel);
                gamePane.getChildren().add(imageView);

                gameField.setLevel(level);

                TileMap tileMap = levelSelector(level, gameField, gamePane);

                EntityManagement entityManagement = new EntityManagement(gamePane, gameField, geldLabel);
                entityManagement.loadCharacter();

                scene = new Scene(root, gameField.getScreenWidth(), gameField.getScreenHeight());

                KeyInputHandler keyInputHandler = new KeyInputHandler();
                keyInputHandler.addKeyHandlers(scene);

                CollisionCheck collisionCheck = new CollisionCheck(tileMap, entityManagement);
                gameLoop = new GameLoop(entityManagement, keyInputHandler, gameSettings, collisionCheck);
                gameLoop.start();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Sanctum of Sorrow - Level " + level);
                soundEngine.playFightMusic();
                entityManagement.loadEntities(collisionCheck);
                break;

            case "LevelSelect":
                loader.setLocation(Main.class.getResource("/FXMLFiles/LevelSelection.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Level Selection");
                break;

            case "GameOver":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameOverScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Game Over");
                soundEngine.stopMusic();
                soundEngine.playGameOver();
                if (gameLoop != null) gameLoop.stop();
                break;

            case "Win":
                loader.setLocation(Main.class.getResource("/FXMLFiles/WinScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - You Win!");
                soundEngine.stopMusic();
                soundEngine.playWin();
                if (gameLoop != null) gameLoop.stop();
                break;

            case "UpgradeScreen":
                loader.setLocation(Main.class.getResource("/FXMLFiles/ShopMenu.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Upgrade Screen");
                break;

            default:
                throw new IllegalArgumentException("Unknown window: " + window);
        }

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static TileMap levelSelector(int level, GameField gameField, Pane gamePane) throws IOException {
        return switch (level) {
            case 1 -> new TileMap("/Tiles/TileMap1.txt", gameField.getTileSize(), gamePane);
            case 2 -> new TileMap("/Tiles/TileMap2.txt", gameField.getTileSize(), gamePane);
            case 3 -> new TileMap("/Tiles/TileMap3.txt", gameField.getTileSize(), gamePane);
            default -> throw new IllegalArgumentException("Unknown level: " + level);
        };
    }

    @Override
    public void stop() {
        if(gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static GameField getGameField() {
        return gameField;
    }

    public static Label getGeldLabel() {
        return geldLabel;
    }

    public static ImageView getImageView() {
        return imageView;
    }
}