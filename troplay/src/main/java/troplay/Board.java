package troplay;

import engine.Resources;
import engine.Sprite;
import java.awt.Point;
import java.util.List;

public class Board extends Sprite {
    private static final String LAYOUT = "/troplay/data/board.csv";

    private final List<Square> squares = loadSquares();
    private final boolean specialSquares;

    public Board(boolean specialSquares) {
        super(new Point(6, 6), Layer.BOARD, "board/plain.png", "board/special.png");
        this.specialSquares = specialSquares;
        setFrame(specialSquares ? 1 : 0);
    }

    private static List<Square> loadSquares() {
        return Resources.lines(LAYOUT).stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(Board::parseSquare)
                .toList();
    }

    private static Square parseSquare(String line) {
        var fields = line.split(",");

        return new Square(
                new Point(Integer.parseInt(fields[0]), Integer.parseInt(fields[1])),
                SpeechBubble.Direction.valueOf(fields[2]),
                Difficulty.valueOf(fields[3]),
                Square.Type.valueOf(fields[4]),
                fields[5].equals("-") ? -1 : Integer.parseInt(fields[5]));
    }

    public Square getSquare(int index) { return squares.get(index); }
    public List<Square> getSquares() { return squares; }
    public int lastIndex() { return squares.size() - 1; }

    public boolean isLadder(int index) { return specialSquares && getSquare(index).getType() == Square.Type.LADDER; }
    public boolean isBrake(int index) { return specialSquares && getSquare(index).getType() == Square.Type.BRAKE; }
    public boolean isGoal(int index) { return getSquare(index).getType() == Square.Type.GOAL; }
}
