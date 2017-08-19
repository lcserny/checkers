package net.cserny.games.checkers;

import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.net.URL;
import java.util.ResourceBundle;

import static net.cserny.games.checkers.CheckersApplication.TILE_SIZE;

/**
 * Created by leonardo on 18.08.2017.
 */
public class Piece extends StackPane
{
    private PieceType type;
    private double mouseX, mouseY;
    private double currentX, currentY;

    public Piece(PieceType type, int x, int y) {
        this.type = type;
        move(x, y);

        Ellipse background = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        background.setFill(Color.BLACK);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(TILE_SIZE * 0.03);
        background.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        background.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        Ellipse foreground = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        foreground.setFill(type == PieceType.RED ? Color.valueOf("#c40003") : Color.valueOf("fff9f4"));
        foreground.setStroke(Color.BLACK);
        foreground.setStrokeWidth(TILE_SIZE * 0.03);
        foreground.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        foreground.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(background, foreground);

        attachEventHandlers();
    }

    private void attachEventHandlers() {
        setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        setOnMouseDragged(event -> {
            relocate(event.getSceneX() - mouseX + currentX, event.getSceneY() - mouseY + currentY);
        });

        GameEngine engine = GameEngine.getInstance();
        Tile[][] board = engine.getBoard();
        setOnMouseReleased(event -> {
            int startX = engine.toBoard(getCurrentX());
            int startY = engine.toBoard(getCurrentY());
            int newX = engine.toBoard(getLayoutX());
            int newY = engine.toBoard(getLayoutY());

            MoveResult result = engine.tryMove(this, newX, newY);
            switch (result.getType()) {
                case NONE:
                    abortMove();
                    break;
                case NORMAL:
                    move(newX, newY);
                    board[startX][startY].setPiece(null);
                    board[newX][newY].setPiece(this);
                    break;
                case KILL:
                    move(newX, newY);
                    board[startX][startY].setPiece(null);
                    board[newX][newY].setPiece(this);

                    Piece otherPiece = result.getPiece();
                    int otherPieceX = engine.toBoard(otherPiece.getCurrentX());
                    int otherPieceY = engine.toBoard(otherPiece.getCurrentY());
                    board[otherPieceX][otherPieceY].setPiece(null);
                    engine.getPiecesGroup().getChildren().remove(otherPiece);
                    break;
            }
        });
    }

    public PieceType getType() {
        return type;
    }

    public double getCurrentX() {
        return currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void move(int x, int y) {
        currentX = x * TILE_SIZE;
        currentY = y * TILE_SIZE;
        relocate(currentX, currentY);
    }

    public void abortMove() {
        relocate(currentX, currentY);
    }
}
