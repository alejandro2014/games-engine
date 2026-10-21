package troplay;

import engine.Engine;
import engine.Images;
import engine.Sprite;
import java.awt.Point;
import java.util.Random;

public class Dice extends Sprite {
    private static final int FACES = 6;
    private static final int ROLL_TICKS = Engine.ticks(2_100);
    private static final int FACE_TICKS = Engine.ticks(70);

    private final Random random = new Random();
    private int value = 1;
    private int rollTicks;

    public Dice() {
        super(new Point(698, 67), Layer.UI,
              Images.numbered("dice", FACES));
    }

    public boolean isRolling() { return rollTicks > 0; }
    public int getValue() { return value; }

    public void roll() {
        value = random.nextInt(FACES) + 1;
        rollTicks = ROLL_TICKS;
    }

    @Override
    public void update() {
        if (rollTicks == 0) {
            return;
        }

        rollTicks--;
        showFace(rollTicks == 0 ? value : rollTicks / FACE_TICKS % FACES + 1);
    }

    private void showFace(int face) {
        setFrame(face - 1);
    }
}
