package jantrix;

import engine.Images;
import java.awt.Image;

/**
 * The seven pieces. Each rotation lists the {column, row} offsets of the four
 * cells relative to the pivot, which is always the first one.
 */
public enum Tetromino {
    LONG(1, 2, new int[][][] {
            {{0, 0}, {0, -2}, {0, -1}, {0, 1}},
            {{0, 0}, {2, 0}, {1, 0}, {-1, 0}}}),
    T(1, 1, new int[][][] {
            {{0, 0}, {0, -1}, {-1, 0}, {0, 1}},
            {{0, 0}, {1, 0}, {0, -1}, {-1, 0}},
            {{0, 0}, {0, 1}, {1, 0}, {0, -1}},
            {{0, 0}, {-1, 0}, {0, 1}, {1, 0}}}),
    SQUARE(4, 1, new int[][][] {
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}}}),
    L_INVERTED(2, 1, new int[][][] {
            {{0, 0}, {-1, -1}, {0, -1}, {0, 1}},
            {{0, 0}, {1, -1}, {1, 0}, {-1, 0}},
            {{0, 0}, {1, 1}, {0, 1}, {0, -1}},
            {{0, 0}, {-1, 1}, {-1, 0}, {1, 0}}}),
    L(2, 1, new int[][][] {
            {{0, 0}, {0, -1}, {1, -1}, {0, 1}},
            {{0, 0}, {1, 0}, {1, 1}, {-1, 0}},
            {{0, 0}, {0, 1}, {-1, 1}, {0, -1}},
            {{0, 0}, {-1, 0}, {-1, -1}, {1, 0}}}),
    S_INVERTED(3, 1, new int[][][] {
            {{0, 0}, {0, -1}, {1, 0}, {1, 1}},
            {{0, 0}, {1, 0}, {0, 1}, {-1, 1}}}),
    S(3, 1, new int[][][] {
            {{0, 0}, {1, -1}, {1, 0}, {0, 1}},
            {{0, 0}, {-1, -1}, {0, -1}, {1, 0}}});

    private static final int SPAWN_COLUMN = 5;

    private final int value;
    private final int spawnRow;
    private final int[][][] rotations;

    Tetromino(int value, int spawnRow, int[][][] rotations) {
        this.value = value;
        this.spawnRow = spawnRow;
        this.rotations = rotations;
    }

    /** The tile of each piece is named after it, e.g. tetromino/l-inverted.png */
    public Image getTile() { return Images.load(Images.path("tetromino", name().toLowerCase().replace('_', '-'))); }
    public int getValue() { return value; }
    public int getSpawnColumn() { return SPAWN_COLUMN; }
    public int getSpawnRow() { return spawnRow; }
    public int getRotationCount() { return rotations.length; }

    public int[][] getCells(int rotation) {
        return rotations[rotation];
    }
}
