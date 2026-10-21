package jantrix;

import engine.Drawable;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

public class Board extends Drawable {
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    public static final int CELL_SIZE = 23;

    private final Tetromino[][] cells = new Tetromino[ROWS][COLUMNS];

    public Board() {
        setXY(16, 16);
        setLayer(Layer.BOARD);
    }

    public boolean fits(Tetromino tetromino, int rotation, int column, int row) {
        for (var cell : tetromino.getCells(rotation)) {
            var c = column + cell[0];
            var r = row + cell[1];
            if (c < 0 || c >= COLUMNS || r < 0 || r >= ROWS || cells[r][c] != null) {
                return false;
            }
        }
        return true;
    }

    public void lock(Tetromino tetromino, int rotation, int column, int row) {
        for (var cell : tetromino.getCells(rotation)) {
            cells[row + cell[1]][column + cell[0]] = tetromino;
        }
    }

    /**
     * Removes the complete lines, moving down the ones above them
     * @return the number of lines removed
     */
    public int clearLines() {
        var removed = 0;

        for (var row = ROWS - 1; row >= 0; row--) {
            if (Arrays.stream(cells[row]).allMatch(cell -> cell != null)) {
                removed++;
            } else if (removed > 0) {
                cells[row + removed] = cells[row];
            }
        }

        for (var row = 0; row < removed; row++) {
            cells[row] = new Tetromino[COLUMNS];
        }
        return removed;
    }

    public Point cellOrigin(int column, int row) {
        return new Point(x + column * CELL_SIZE, y + row * CELL_SIZE);
    }

    @Override
    public void draw(Graphics2D g) {
        for (var row = 0; row < ROWS; row++) {
            for (var column = 0; column < COLUMNS; column++) {
                var cell = cells[row][column];
                if (cell != null) {
                    drawTile(g, cell.getTile(), cellOrigin(column, row));
                }
            }
        }
    }

    static void drawTile(Graphics2D g, Image tile, Point origin) {
        g.drawImage(tile, origin.x, origin.y, null);
    }
}
