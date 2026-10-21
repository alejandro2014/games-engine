package jantrix;

import engine.Drawable;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

/**
 * Preview of the piece that will appear next, centered in the box of the background
 */
public class NextPiece extends Drawable {
    private Tetromino tetromino;

    public NextPiece() {
        setXY(303, 125);
        setLayer(Layer.HUD);
    }

    public void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
    }

    @Override
    public void draw(Graphics2D g) {
        var cells = tetromino.getCells(0);
        var minColumn = Arrays.stream(cells).mapToInt(cell -> cell[0]).min().orElseThrow();
        var maxColumn = Arrays.stream(cells).mapToInt(cell -> cell[0]).max().orElseThrow();
        var minRow = Arrays.stream(cells).mapToInt(cell -> cell[1]).min().orElseThrow();
        var maxRow = Arrays.stream(cells).mapToInt(cell -> cell[1]).max().orElseThrow();

        var left = x - (maxColumn - minColumn + 1) * Board.CELL_SIZE / 2;
        var top = y - (maxRow - minRow + 1) * Board.CELL_SIZE / 2;
        var tile = tetromino.getTile();

        for (var cell : cells) {
            Board.drawTile(g, tile, new Point(left + (cell[0] - minColumn) * Board.CELL_SIZE,
                                              top + (cell[1] - minRow) * Board.CELL_SIZE));
        }
    }
}
