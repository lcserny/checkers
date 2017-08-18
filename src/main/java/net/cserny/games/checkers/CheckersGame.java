package net.cserny.games.checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by leonardo on 18.08.2017.
 */
public class CheckersGame extends Application
{
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tilesGroup = new Group();
    private Group piecesGroup = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContext() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tilesGroup, piecesGroup);

        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < HEIGHT; x++) {
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

        return root;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY) {
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

            MoveResult result = tryMove(piece, newX, newY);

            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

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

    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContext());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
