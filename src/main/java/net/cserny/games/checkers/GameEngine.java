package net.cserny.games.checkers;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import static net.cserny.games.checkers.CheckersApplication.HEIGHT;
import static net.cserny.games.checkers.CheckersApplication.TILE_SIZE;
import static net.cserny.games.checkers.CheckersApplication.WIDTH;

/**
 * Created by leonardo on 18.08.2017.
 */
public class GameEngine
{
    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Group tilesGroup = new Group();
    private Group piecesGroup = new Group();

    public Parent createContext() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((y + x) % 2 == 0, x, y);
                board[x][y] = tile;
                tilesGroup.getChildren().add(tile);

                Piece piece = null;
                if (y <= 2 && (y + x) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }

                if (y >= 5 && (y + x) % 2 != 0) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    piecesGroup.getChildren().add(piece);
                }
            }
        }

        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tilesGroup, piecesGroup);

        return root;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY) {
        if ((newX < 0 || newX > WIDTH - 1) || (newY < 0 || newY > HEIGHT - 1)) {
            return new MoveResult(MoveType.NONE);
        }

        if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDirection) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDirection * 2) {
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;
            if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);
        piece.setOnMouseReleased(event -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            MoveResult result = tryMove(piece, newX, newY);
            switch (result.getType()) {
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
                case KILL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);

                    Piece otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    piecesGroup.getChildren().remove(otherPiece);
                    break;
            }
        });

        return piece;
    }
}
