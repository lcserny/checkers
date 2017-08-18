package net.cserny.games.checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by leonardo on 18.08.2017.
 */
public class CheckersApplication extends Application
{
    private GameEngine engine = new GameEngine();

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(engine.createContext());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
