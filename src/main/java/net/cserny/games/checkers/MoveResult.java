package net.cserny.games.checkers;

/**
 * Created by leonardo on 18.08.2017.
 */
public class MoveResult
{
    private MoveType type;
    private Piece piece;

    public MoveType getType() {
        return type;
    }

    public Piece getPiece() {
        return piece;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }
}
