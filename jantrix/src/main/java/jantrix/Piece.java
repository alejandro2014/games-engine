package jantrix;

import engine.Drawable;
import java.awt.Graphics2D;

/**
 * The piece that is falling through the board
 */
public class Piece extends Drawable {
    private final Board board;
    private Tetromino tetromino;
    private int rotation;
    private int column;
    private int row;

    public Piece(Board board) {
        this.board = board;
        setLayer(Layer.PIECE);
    }

    /**
     * Places a new piece at the top of the board
     * @return false if there is no room for it, which ends the game
     */
    public boolean spawn(Tetromino tetromino) {
        this.tetromino = tetromino;
        rotation = 0;
        column = tetromino.getSpawnColumn();
        row = tetromino.getSpawnRow();
        return board.fits(tetromino, rotation, column, row);
    }

    public boolean move(int columns, int rows) {
        if (!board.fits(tetromino, rotation, column + columns, row + rows)) {
            return false;
        }

        column += columns;
        row += rows;
        return true;
    }

    public void turn() {
        var next = (rotation + 1) % tetromino.getRotationCount();
        if (board.fits(tetromino, next, column, row)) {
            rotation = next;
        }
    }

    public void lock() {
        board.lock(tetromino, rotation, column, row);
    }

    public Tetromino getTetromino() { return tetromino; }

    @Override
    public void draw(Graphics2D g) {
        var tile = tetromino.getTile();
        for (var cell : tetromino.getCells(rotation)) {
            Board.drawTile(g, tile, board.cellOrigin(column + cell[0], row + cell[1]));
        }
    }
}
