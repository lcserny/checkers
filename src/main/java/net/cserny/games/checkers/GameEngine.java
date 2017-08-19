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

        int startX = toBoard(piece.getCurrentX());
        int startY = toBoard(piece.getCurrentY());

        if (Math.abs(newX - startX) == 1 && newY - startY == piece.getType().moveDirection) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - startX) == 2 && newY - startY == piece.getType().moveDirection * 2) {
            int betweenX = startX + (newX - startX) / 2;
            int betweenY = startY + (newY - startY) / 2;
            Tile betweenTile = board[betweenX][betweenY];
            if (betweenTile.hasPiece() && betweenTile.getPiece().getType() != piece.getType()) {
                return new MoveResult(MoveType.KILL, betweenTile.getPiece());
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
            int startX = toBoard(piece.getCurrentX());
            int startY = toBoard(piece.getCurrentY());
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult result = tryMove(piece, newX, newY);
            switch (result.getType()) {
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    board[startX][startY].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
                case KILL:
                    piece.move(newX, newY);
                    board[startX][startY].setPiece(null);
                    board[newX][newY].setPiece(piece);

                    Piece otherPiece = result.getPiece();
                    int otherPieceX = toBoard(otherPiece.getCurrentX());
                    int otherPieceY = toBoard(otherPiece.getCurrentY());
                    board[otherPieceX][otherPieceY].setPiece(null);
                    piecesGroup.getChildren().remove(otherPiece);
                    break;
            }
        });

        return piece;
    }
}
