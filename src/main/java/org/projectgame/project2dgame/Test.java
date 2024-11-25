package org.projectgame.project2dgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.projectgame.project2dgame.Controller.GameFieldController;
import org.projectgame.project2dgame.Controller.MainMenuController;
import org.projectgame.project2dgame.GameField.GameField;

import java.io.IOException;

public class Test extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/FXMLFiles/MainMenu.fxml"));
        Parent root = loader.load();
        GameField gameField = new GameField();
        Scene scene = new Scene(root, 300, 400);
        stage.setTitle("2D Action-RPG");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
