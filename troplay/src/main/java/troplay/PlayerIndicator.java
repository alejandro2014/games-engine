package troplay;

import engine.Engine;
import engine.Images;
import engine.Sprite;
import java.awt.Point;

public class PlayerIndicator extends Sprite {
    private static final int FRAMES = 5;
    private static final int FRAME_TICKS = Engine.ticks(70);

    private boolean active;
    private int frameTicks;
    private int animFrame = -1;
    private int animStep = 1;

    public PlayerIndicator(int playerId) {
        super(new Point(691 + playerId * 63, 554), Layer.UI,
              Images.numbered("player/" + (playerId + 1) + "/indicator", FRAMES));
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update() {
        if (!active) {
            setFrame(0);
            return;
        }
        if (++frameTicks < FRAME_TICKS) {
            return;
        }
        frameTicks = 0;

        animFrame += animStep;
        switch (animFrame) {
            case 5 -> {
                animFrame = 3;
                animStep = -1;
            }
            case -1 -> {
                animFrame = 1;
                animStep = 1;
            }
            default -> {
            }
        }

        setFrame(animFrame);
    }
}
