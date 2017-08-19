package net.cserny.games.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static net.cserny.games.checkers.CheckersApplication.TILE_SIZE;

/**
 * Created by leonardo on 18.08.2017.
 */
public class Tile extends Rectangle
{
    private Piece piece;

    public Tile(boolean light, int x, int y) {
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("feb") : Color.valueOf("582"));
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
