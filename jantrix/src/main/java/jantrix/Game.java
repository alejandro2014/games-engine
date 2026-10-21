package jantrix;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.stream.IntStream;

public class Game extends Screen {
    // Time between each step down of the piece, by level
    private static final int[] DROP_TICKS = IntStream.of(800, 700, 600, 500, 300, 200, 100, 75, 50, 25)
            .map(Engine::ticks)
            .toArray();
    private static final int MAX_LEVEL = DROP_TICKS.length - 1;
    private static final int LINES_PER_LEVEL = 20;
    private static final int LINE_VALUE = 10;

    private final int startLevel;
    private final Random random = new Random();
    private final Board board = new Board();
    private final Piece piece = new Piece(board);
    private final NextPiece nextPiece = new NextPiece();
    private final Hud hud = new Hud();
    private final GameOverMessage gameOverMessage = new GameOverMessage();

    private Tetromino next;
    private int level;
    private int score;
    private int lines;
    private int ticks;
    private boolean gameOver;

    public Game(Engine engine, int startLevel) {
        super(engine);
        this.startLevel = startLevel;
        level = startLevel;

        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "game/background.png"));
        scene.add(board);
        scene.add(piece);
        scene.add(nextPiece);
        scene.add(hud);
        scene.add(gameOverMessage);

        next = randomTetromino();
        newPiece();
    }

    @Override
    protected void update() {
        if (!gameOver && ++ticks >= DROP_TICKS[level]) {
            stepDown();
        }
    }

    @Override
    protected void onKey(int keyCode) {
        if (gameOver) {
            if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
                backToMenu();
            }
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_UP -> piece.turn();
            case KeyEvent.VK_DOWN -> stepDown();
            case KeyEvent.VK_LEFT -> piece.move(-1, 0);
            case KeyEvent.VK_RIGHT -> piece.move(1, 0);
            case KeyEvent.VK_ESCAPE -> backToMenu();
            default -> {
            }
        }
    }

    private void stepDown() {
        ticks = 0;
        if (!piece.move(0, 1)) {
            lockPiece();
        }
    }

    private void lockPiece() {
        piece.lock();
        score += (level + 1) * piece.getTetromino().getValue();

        var removed = board.clearLines();
        lines += removed;
        score += LINE_VALUE * (level + 1) * removed;
        level = Math.max(startLevel, Math.min(MAX_LEVEL, lines / LINES_PER_LEVEL));

        newPiece();
    }

    private void newPiece() {
        if (!piece.spawn(next)) {
            gameOver = true;
            piece.setVisible(false);
            gameOverMessage.setVisible(true);
        }

        next = randomTetromino();
        nextPiece.setTetromino(next);
        hud.set(level, score, lines);
    }

    private Tetromino randomTetromino() {
        var tetrominoes = Tetromino.values();
        return tetrominoes[random.nextInt(tetrominoes.length)];
    }

    private void backToMenu() {
        fire(AppEvent.BACK);
    }
}
