package net.cserny.games.checkers;

import javafx.scene.Scene;

/**
 * Created by leonardo on 19.08.2017.
 */
public class GameScene extends Scene
{
    private GameEngine engine;

    public GameScene(GameEngine engine) {
        super(engine.createContext());
    }

    public GameEngine getEngine() {
        return engine;
    }
}
