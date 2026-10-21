package engine.input;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputDispatcher {
    private final List<MouseTarget> targets = new CopyOnWriteArrayList<>();
    private MouseTarget pressed;
    private MouseTarget hovered;
    private boolean enabled = true;

    public void add(MouseTarget target) {
        targets.add(target);
    }

    public void remove(MouseTarget target) {
        targets.remove(target);
        if (pressed == target) {
            pressed = null;
        }
        if (hovered == target) {
            hovered = null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            cancel();
        }
    }

    public void dispatch(Mouse mouse) {
        for (var event = mouse.poll(); event != null; event = mouse.poll()) {
            if (enabled) {
                handle(event);
            }
        }
    }

    private void handle(Mouse.Event event) {
        var point = event.point();

        switch (event.type()) {
            case MOVED -> hover(point);
            case PRESSED -> {
                hover(point);
                pressed = targetAt(point);
                if (pressed != null) {
                    pressed.onPress(point);
                }
            }
            case RELEASED -> {
                if (pressed == null) {
                    return;
                }

                var target = pressed;
                var inside = target.contains(point);
                pressed = null;
                target.onRelease(point, inside);
                if (inside) {
                    target.onClick(point);
                }
            }
        }
    }

    private void hover(Point point) {
        var target = targetAt(point);
        if (target == hovered) {
            return;
        }

        if (hovered != null) {
            hovered.onExit();
        }
        hovered = target;
        if (hovered != null) {
            hovered.onEnter();
        }
    }

    private void cancel() {
        if (pressed != null) {
            var target = pressed;
            pressed = null;
            target.onRelease(null, false);
        }
        if (hovered != null) {
            hovered.onExit();
            hovered = null;
        }
    }

    private MouseTarget targetAt(Point point) {
        return targets.stream()
                .filter(target -> target.contains(point))
                .max(Comparator.comparingInt(MouseTarget::getZ))
                .orElse(null);
    }
}
