package org.projectgame.project2dgame;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
    private static Label geldLabel;
    private static Label timeLabel;
    private static ImageView imageView;
    private static Pane pausePane;
    private static GameFieldController gameFieldController;
    private static Stage pauseStage;


    @Override
    public void start(Stage stage) throws IOException {
        CharacterInfo.init();
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
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
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                break;

            case "GameField":
                if (gameLoop != null) {
                    gameLoop.stop();
                    gameLoop = null;
                }
                if (gameFieldController != null){
                    gameFieldController = null;
                }
                scene = null;
                loadGameFieldAsync(level);
                break;

            case "LevelSelect":
                loader.setLocation(Main.class.getResource("/FXMLFiles/LevelSelection.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Level Selection");
                if(!SoundEngine.isPlaying()) SoundEngine.playMainMenuMusic();
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                break;

            case "GameOver":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameOverScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Game Over");
                SoundEngine.stopMusic();
                SoundEngine.playGameOver();
                if (gameLoop != null) {
                    gameLoop.stop();
                    gameLoop = null;
                }
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                break;

            case "Win":
                loader.setLocation(Main.class.getResource("/FXMLFiles/WinScreen.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - You Win!");
                SoundEngine.stopMusic();
                SoundEngine.playWin();
                if (gameLoop != null) {
                    gameLoop.stop();
                    gameLoop = null;
                }
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                break;

            default:
                throw new IllegalArgumentException("Unknown window: " + window);
        }
    }

    // GameField asynchron laden
    public static void loadGameFieldAsync(int level) {
        Task<Void> loadTask = new Task<>() {
            private Parent root;
            private Scene scene;
            private TileMap tileMap;
            private EntityManagement entityManagement;
            private CollisionCheck collisionCheck;
            private KeyInputHandler keyInputHandler;

            @Override
            protected Void call() throws Exception {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/GameField.fxml"));
                root = loader.load();
                gameFieldController = loader.getController();

                Pane gamePane = gameFieldController.getGamePane();
                geldLabel = gameFieldController.getGeldLabel();
                imageView = gameFieldController.getImageView();
                timeLabel = gameFieldController.getTimeLabel();
                pausePane = gameFieldController.getPausePane();

                tileMap = levelSelector(level, gamePane);

                entityManagement = new EntityManagement(gamePane, geldLabel, level);
                entityManagement.loadImageCache();
                entityManagement.loadCharacter();

                collisionCheck = new CollisionCheck(tileMap, entityManagement);
                keyInputHandler = new KeyInputHandler();

                scene = new Scene(root, GameField.getScreenWidth(), GameField.getScreenHeight());
                keyInputHandler.addKeyHandlers(scene);

                return null;
            }

            @Override
            protected void succeeded() {
                gameLoop = new GameLoop(entityManagement, keyInputHandler, collisionCheck, timeLabel);
                entityManagement.setCollisonCheck(collisionCheck);
                entityManagement.createProjectileManagement();

                Pane gamePane = gameFieldController.getGamePane();
                gamePane.getChildren().addAll(geldLabel, imageView, timeLabel);

                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Sanctum of Sorrow - Level " + level);
                SoundEngine.playFightMusic();

                PauseTransition delay = new PauseTransition(Duration.millis(200));
                delay.setOnFinished(e -> {
                    entityManagement.loadEntities(collisionCheck);
                });
                delay.play();

                gameLoop.start();
            }
        };

        new Thread(loadTask).start();
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

    public static void openPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/PauseMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Sanctum of Sorrow - Pause Screen");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            pauseStage = stage;
            stage.show();
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

    public static void openSettingsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/SettingsScreen.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Sanctum of Sorrow - Settings Screen");
            scene.getRoot().setFocusTraversable(true);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
            if(!SoundEngine.isPlaying()) SoundEngine.playMainMenuMusic();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static TileMap levelSelector(int level, Pane gamePane) throws IOException {
        return switch (level) {
            case 0 -> new TileMap("/Tiles/TileMap0.txt", GameField.getTileSize(), gamePane);
            case 1 -> new TileMap("/Tiles/TileMap1.txt", GameField.getTileSize(), gamePane);
            case 2 -> new TileMap("/Tiles/TileMap2.txt", GameField.getTileSize(), gamePane);
            case 3 -> new TileMap("/Tiles/TileMap3.txt", GameField.getTileSize(), gamePane);
            case 4 -> new TileMap("/Tiles/TileMap4.txt", GameField.getTileSize(), gamePane);
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

    public static GameLoop getGameLoop() {
        return gameLoop;
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

    public static Pane getPausePane() {
        return pausePane;
    }

    public static Stage getPauseStage() { return  (Stage) pauseStage; }

    public static void pauseGame() {
        pauseGameloop(true);
        pausePane.setVisible(true);
        openPauseMenu();
    }

    public static void pauseGameloop(boolean bol) {
        if (gameLoop != null) {
            gameLoop.setPaused(bol);
        }
    }

    public static boolean isGameLoopPaused() {
        return gameLoop.isPaused();
    }
}