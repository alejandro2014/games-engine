package engine.input;

import java.awt.Point;

public interface MouseTarget {
    boolean contains(Point point);

    int getZ();

    default void onPress(Point point) {
    }

    default void onRelease(Point point, boolean inside) {
    }

    default void onClick(Point point) {
    }

    default void onEnter() {
    }

    default void onExit() {
    }
}
