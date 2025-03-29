package org.projectgame.project2dgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Controller.GameFieldController;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Controller.SoundEngine;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.GameLoop;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static GameLoop gameLoop;
    private static final GameField gameField = new GameField();
    private static Label geldLabel;
    private static Label timeLabel;
    private static ImageView imageView;

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

       Debug debug = new Debug();

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
                SoundEngine.playMainMenuMusic();
                break;

            case "GameField":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameField.fxml"));
                Parent root = loader.load();
                GameFieldController controller = loader.getController();
                Pane gamePane = controller.getGamePane();
                geldLabel = controller.getGeldLabel();
                imageView = controller.getImageView();
                timeLabel = controller.getTimeLabel();
                gamePane.getChildren().add(geldLabel);
                gamePane.getChildren().add(imageView);
                gamePane.getChildren().add(timeLabel);

                TileMap tileMap = levelSelector(level, gameField, gamePane);

                EntityManagement entityManagement = new EntityManagement(gamePane, gameField, geldLabel, level);
                entityManagement.loadCharacter();

                scene = new Scene(root, gameField.getScreenWidth(), gameField.getScreenHeight());

                KeyInputHandler keyInputHandler = new KeyInputHandler();
                keyInputHandler.addKeyHandlers(scene);

                CollisionCheck collisionCheck = new CollisionCheck(tileMap, entityManagement);
                gameLoop = new GameLoop(entityManagement, keyInputHandler, collisionCheck, timeLabel);
                gameLoop.start();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Sanctum of Sorrow - Level " + level);
                SoundEngine.playFightMusic();
                entityManagement.loadEntities(collisionCheck);
                break;

            case "LevelSelect":
                loader.setLocation(Main.class.getResource("/FXMLFiles/LevelSelection.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Level Selection");
                if(!SoundEngine.isPlaying()) SoundEngine.playMainMenuMusic();
                break;

            case "GameOver":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameOverScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Game Over");
                SoundEngine.stopMusic();
                SoundEngine.playGameOver();
                if (gameLoop != null) gameLoop.stop();
                break;

            case "Win":
                loader.setLocation(Main.class.getResource("/FXMLFiles/WinScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - You Win!");
                SoundEngine.stopMusic();
                SoundEngine.playWin();
                if (gameLoop != null) gameLoop.stop();
                break;

            case "SettingsScreen":
                loader.setLocation(Main.class.getResource("/FXMLFiles/SettingsScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Settings");
                scene.getRoot().setFocusTraversable(true);
                SoundEngine.stopMusic();
                SoundEngine.playMainMenuMusic();
                break;

            default:
                throw new IllegalArgumentException("Unknown window: " + window);
        }

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void openUpgradeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/ShopMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Sanctum of Sorrow - Upgrade Screen");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.show();
            SoundEngine.stopMusic();
            SoundEngine.playShopMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openBestenlisteWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/BestenListe.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Sanctum of Sorrow - Bestenlisten");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static TileMap levelSelector(int level, GameField gameField, Pane gamePane) throws IOException {
        return switch (level) {
            case 1 -> new TileMap("/Tiles/TileMap1.txt", gameField.getTileSize(), gamePane);
            case 2 -> new TileMap("/Tiles/TileMap2.txt", gameField.getTileSize(), gamePane);
            case 3 -> new TileMap("/Tiles/TileMap3.txt", gameField.getTileSize(), gamePane);
            default -> throw new IllegalArgumentException("Unknown level: " + level);
        };
    }

    public static void safeGameTime(int level) throws IOException {
        Double time = gameLoop.getRawGameTimeSeconds();
        GameSettings.saveTime(level, time);
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

    public static Label getTimeLabel() {
        return timeLabel;
    }
}