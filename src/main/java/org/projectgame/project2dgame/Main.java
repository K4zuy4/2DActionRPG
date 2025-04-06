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
import org.projectgame.project2dgame.Controller.*;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Data.StoryCutscene;
import org.projectgame.project2dgame.Entities.CharacterInfo;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.EndlessGameManager;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.GameLoop;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.io.IOException;
import java.util.Random;

public class Main extends Application {

    private static Stage primaryStage;
    private static GameLoop gameLoop;
    private static Label geldLabel;
    private static Label timeLabel;
    private static ImageView imageView;
    private static Pane pausePane;
    private static GameFieldController gameFieldController;
    private static Stage pauseStage;
    private static EndlessGameManager endlessGameManager;
    private static boolean endlessMode = false;
    private static EntityManagement entityManagement;


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
                GameOverEndlessScreenController controller = new GameOverEndlessScreenController();
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

            case "GameOverEndless":
                loader.setLocation(Main.class.getResource("/FXMLFiles/GameOverEndless.fxml"));
                scene = new Scene(loader.load());
                GameOverEndlessScreenController gameOverEndlessScreenController = loader.getController();
                gameOverEndlessScreenController.setWaveCount(EndlessGameManager.getWaveCount());
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
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Sanctum of Sorrow - Level " + level);

                gameLoop = new GameLoop(entityManagement, keyInputHandler, collisionCheck, timeLabel);
                entityManagement.setCollisonCheck(collisionCheck);
                entityManagement.createProjectileManagement();

                Pane gamePane = gameFieldController.getGamePane();
                gamePane.getChildren().addAll(geldLabel, imageView, timeLabel);

                gameLoop.start();
                gameLoop.setPaused(true);

                if (level == 1) {
                    // Storycutscene abspielen
                    SoundEngine.playAmbientSound();
                    entityManagement.getCharacter().setSpawnStill();
                    entityManagement.getCharacter().updateHitboxPosition();
                    StoryCutscene cutscene = new StoryCutscene(gamePane);
                    cutscene.setOnFinished(() -> {
                        entityManagement.getCharacter().playSpawnAnimation();
                        PauseTransition delay = new PauseTransition(Duration.millis(1500));
                        delay.setOnFinished(e -> {
                            entityManagement.loadEntities(collisionCheck);
                            SoundEngine.playFightMusic();
                        });
                        delay.play();
                    });
                } else {
                    if(level == 5) SoundEngine.playBossMusic();
                    else SoundEngine.playFightMusic();
                    entityManagement.getCharacter().updateHitboxPosition();
                    PauseTransition delay = new PauseTransition(Duration.millis(1500));
                    delay.setOnFinished(e -> {
                        entityManagement.loadEntities(collisionCheck);
                    });
                    delay.play();
                }
            }

        };

        new Thread(loadTask).start();
    }

    public static void startEndlessMode() throws IOException {
        endlessMode = true;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/GameField.fxml"));
        Parent root = loader.load();
        gameFieldController = loader.getController();

        geldLabel = gameFieldController.getGeldLabel();
        imageView = gameFieldController.getImageView();
        timeLabel = gameFieldController.getTimeLabel();
        pausePane = gameFieldController.getPausePane();

        Pane gamePane = gameFieldController.getGamePane();

        Random ran = new Random();
        TileMap tileMap = new TileMap("/Tiles/TileMap" + (ran.nextInt(5) + 1) +".txt", GameField.getTileSize(), gamePane);

        EntityManagement entityManagement = new EntityManagement(gamePane, geldLabel, 0);
        entityManagement.loadImageCache();
        entityManagement.loadCharacter();
        CollisionCheck collisionCheck = new CollisionCheck(tileMap, entityManagement);
        KeyInputHandler keyInputHandler = new KeyInputHandler();

        Scene scene = new Scene(root, GameField.getScreenWidth(), GameField.getScreenHeight());
        keyInputHandler.addKeyHandlers(scene);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Sanctum of Sorrow - Endless Mode");

        gameLoop = new GameLoop(entityManagement, keyInputHandler, collisionCheck, timeLabel);
        endlessGameManager = new EndlessGameManager(entityManagement);

        entityManagement.setCollisonCheck(collisionCheck);
        entityManagement.createProjectileManagement();
        gamePane.getChildren().addAll(geldLabel, imageView, timeLabel);

        gameLoop.start();
        gameLoop.setPaused(true);

        entityManagement.getCharacter().setSpawnStill();
        entityManagement.getCharacter().updateHitboxPosition();

        PauseTransition spawnDelay = new PauseTransition(Duration.millis(500));
        spawnDelay.setOnFinished(e -> {
            entityManagement.getCharacter().playSpawnAnimation();
            Main.getEndlessGameManager().startNextWave();

            PauseTransition startWaveDelay = new PauseTransition(Duration.millis(2000));
            startWaveDelay.setOnFinished(event -> {
                SoundEngine.playFightMusic();
            });
            startWaveDelay.play();
        });
        spawnDelay.play();

        SoundEngine.playAmbientSound();
    }

    public static EndlessGameManager getEndlessGameManager() {
        return endlessGameManager;
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
            stage.setOnCloseRequest(event -> {
                if (Main.getEndlessGameManager() != null) {
                    Main.getEndlessGameManager().setWaitingForUpgrade(false);
                    SoundEngine.playFightMusic();
                    Main.pauseGameloop(false);
                }

                if (EndlessGameManager.getEntityManagement() != null) {
                    EndlessGameManager.getEntityManagement().reloadCharacter();
                }
            });
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
            case 5 -> new TileMap("/Tiles/TileMap5.txt", GameField.getTileSize(), gamePane);
            default -> throw new IllegalArgumentException("Unknown level: " + level);
        };
    }

    public static void safeGameTime(int level) throws IOException {
        Double time = gameLoop.getRawGameTimeSeconds();
        GameSettings.saveTime(level, time);
    }

    public static void safeEndlessGameTime(int wave) throws IOException {
        Double time = gameLoop.getRawGameTimeSeconds();
        GameSettings.saveWave(wave, time);
    }

    @Override
    public void stop() {
        if(gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void resetEndlessMode() {
        endlessMode = false;
        endlessGameManager = null;
        entityManagement = null;
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

    public static boolean isEndlessMode() {
        return endlessMode;
    }

    public static Pane getGamePane() {
        return gameFieldController.getGamePane();
    }

    public static boolean endlessMode() {
        return endlessMode;
    }

    public static void setEndlessMode(boolean endlessMode) {
        Main.endlessMode = endlessMode;
    }
}