package org.projectgame.project2dgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    private static SoundEngine soundEngine = new SoundEngine();

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

        GameSettings gameSettings = new GameSettings();
        Debug debug = new Debug(gameSettings);

        primaryStage.show();
    }

    public static void setWindow(String window, int level) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scene scene = null;
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
                GameField gameField = new GameField();

                TileMap tileMap = levelSelector(level, gameField, gamePane);

                EntityManagement entityManagement = new EntityManagement(gamePane, gameField);
                entityManagement.loadEntities();
                entityManagement.loadCharacter();


                GameSettings gameSettings = new GameSettings();
                scene = new Scene(root, gameField.getScreenWidth(), gameField.getScreenHeight());

                KeyInputHandler keyInputHandler = new KeyInputHandler(entityManagement);
                keyInputHandler.addKeyHandlers(scene);

                gameLoop = new GameLoop(entityManagement, keyInputHandler, gameSettings, tileMap);
                controller.setGameLoop(gameLoop);
                gameLoop.start();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Sanctum of Sorrow - Level " + level);
                break;

            case "LevelSelect":
                loader.setLocation(Main.class.getResource("/FXMLFiles/LevelSelection.fxml"));
                scene = new Scene(loader.load());
                primaryStage.setTitle("Sanctum of Sorrow - Level Selection");
                break;

            default:
                throw new IllegalArgumentException("Unknown window: " + window);
        }

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static TileMap levelSelector(int level, GameField gameField, Pane gamePane) throws IOException {
        switch(level) {
            case 1:
                TileMap tileMap = new TileMap("/Tiles/TileMap1.txt", gameField.getTileSize(), gamePane);
                return tileMap;
            case 2:
                TileMap tileMap2 = new TileMap("/Tiles/TileMap2.txt", gameField.getTileSize(), gamePane);
                return tileMap2;
            case 3:
                TileMap tileMap3 = new TileMap("/Tiles/TileMap3.txt", gameField.getTileSize(), gamePane);
                return tileMap3;
            default:
                throw new IllegalArgumentException("Unknown level: " + level);
        }
    }

    public GameLoop getGameLoop() {
        return gameLoop;
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
}