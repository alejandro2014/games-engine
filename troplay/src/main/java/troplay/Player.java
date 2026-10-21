package troplay;

import engine.Engine;
import engine.Images;
import engine.Sprite;
import java.awt.Point;

public class Player extends Sprite {
    private static final Point[] OFFSETS = {new Point(6, 0), new Point(12, 6), new Point(6, 12), new Point(0, 6)};
    private static final int TICKS_PER_SQUARE = Engine.ticks(490);
    private static final int LADDER_TICKS = Engine.ticks(1_050);

    private final Board board;
    private final int playerId;

    private int square;
    private int target;
    private boolean ladderUsed;
    private boolean rollAllowed = true;

    private boolean moving;
    private Point from;
    private Point to;
    private int tick;
    private int duration;

    public Player(Board board, int playerId) {
        super(new Point(), Layer.PLAYER, Images.path("player", String.valueOf(playerId + 1), "token"));
        this.board = board;
        this.playerId = playerId;
        setCoords(standingPoint(square));
    }

    public boolean isMoving() { return moving; }
    public int getSquare() { return square; }
    public boolean canRoll() { return rollAllowed; }
    public void setCanRoll(boolean rollAllowed) { this.rollAllowed = rollAllowed; }

    public void advance(int squares) {
        target = Math.min(square + squares, board.lastIndex());
        ladderUsed = false;
        nextMove();
    }

    @Override
    public void update() {
        if (!moving) {
            return;
        }

        tick++;
        setXY(from.x + (to.x - from.x) * tick / duration,
              from.y + (to.y - from.y) * tick / duration);

        if (tick == duration) {
            nextMove();
        }
    }

    private void nextMove() {
        if (square < target) {
            glide(standingPoint(square), standingPoint(square + 1), TICKS_PER_SQUARE);
            square++;
        } else if (!ladderUsed && board.isLadder(square)) {
            var ladder = board.getSquare(square);
            glide(ladder.getPosition(), board.getSquare(ladder.getLink()).getPosition(), LADDER_TICKS);
            square = ladder.getLink() + 1;
            target = square;
            ladderUsed = true;
        } else {
            moving = false;
            setCoords(standingPoint(square));
        }
    }

    private void glide(Point start, Point end, int ticks) {
        from = start;
        to = end;
        tick = 0;
        duration = ticks;
        moving = true;
        setCoords(start);
    }

    private Point standingPoint(int index) {
        var position = board.getSquare(index).getPosition();
        var offset = OFFSETS[playerId];
        return new Point(position.x + offset.x, position.y + offset.y);
    }
}
