package net.cserny.games.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by leonardo on 18.08.2017.
 */
public class Tile extends Rectangle
{
    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(CheckersGame.TILE_SIZE);
        setHeight(CheckersGame.TILE_SIZE);
        relocate(x * CheckersGame.TILE_SIZE, y * CheckersGame.TILE_SIZE);
        setFill(light ? Color.valueOf("feb") : Color.valueOf("582"));
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
