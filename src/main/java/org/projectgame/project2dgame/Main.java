package org.projectgame.project2dgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Controller.GameFieldController;
import org.projectgame.project2dgame.Controller.KeyInputHandler;
import org.projectgame.project2dgame.Data.GameSettings;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import org.projectgame.project2dgame.GameField.GameLoop;
import org.projectgame.project2dgame.GameField.TileManagement.TileMap;

import java.io.IOException;

public class Main extends Application {

    private GameLoop gameLoop;

    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/GameField.fxml"));
        Parent root = loader.load();

        GameFieldController controller = loader.getController();
        Pane gamePane = controller.getGamePane();


        GameField gameField = new GameField();
        TileMap tileMap = new TileMap("/Tiles/TileMap.txt", gameField.getTileSize(), gamePane);

        EntityManagement entityManagement = new EntityManagement(gamePane, tileMap, gameField);
        entityManagement.loadEntities();
        entityManagement.loadCharacter();

        GameSettings gameSettings = new GameSettings();

        Scene scene = new Scene(root, gameField.getScreenWidth(), gameField.getScreenHeight());
        stage.setTitle("2D Action-RPG");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));

        KeyInputHandler keyInputHandler = new KeyInputHandler(entityManagement);
        keyInputHandler.addKeyHandlers(scene);

        gameLoop = new GameLoop(entityManagement, keyInputHandler, gameSettings);
        gameLoop.start();

        Debug debug = new Debug(gameSettings);

        stage.show();
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