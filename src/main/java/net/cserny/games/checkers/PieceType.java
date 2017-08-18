package net.cserny.games.checkers;

/**
 * Created by leonardo on 18.08.2017.
 */
public enum PieceType
{
    RED(1),
    WHITE(-1);

    final int moveDirection;

    PieceType(int moveDirection) {
        this.moveDirection = moveDirection;
    }
}
