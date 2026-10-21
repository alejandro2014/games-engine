package engine.ui;

import engine.Sprite;
import engine.input.MouseTarget;
import java.awt.Point;

public class Button extends Sprite implements MouseTarget {
    private final Runnable action;
    private boolean pressed;

    public Button(Point coords, Enum<?> layer, Runnable action, String... frames) {
        super(coords, layer, frames);
        this.action = action;
    }

    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void onPress(Point point) {
        pressed = true;
        refresh();
    }

    @Override
    public void onRelease(Point point, boolean inside) {
        pressed = false;
        refresh();
    }

    @Override
    public void onClick(Point point) {
        action.run();
    }

    protected void refresh() {
        setFrame(frameIndex(pressed));
    }

    protected int frameIndex(boolean pressed) {
        return pressed ? 1 : 0;
    }
}
