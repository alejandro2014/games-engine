package engine;

import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scene {
    private static final Comparator<Drawable> BY_Z = Comparator.comparingInt(Drawable::getZ);

    private final List<Drawable> drawables = new CopyOnWriteArrayList<>();
    private Drawable background;

    public void setBackground(Drawable background) { this.background = background; }
    public void add(Drawable drawable) { drawables.add(drawable); }
    public void remove(Drawable drawable) { drawables.remove(drawable); }

    public void update() {
        if (background != null) {
            background.update();
        }
        drawables.forEach(Drawable::update);
    }

    public void draw(Graphics2D g) {
        if (background != null && background.isVisible()) {
            background.draw(g);
        }

        drawables.stream()
                .filter(Drawable::isVisible)
                .sorted(BY_Z)
                .forEach(drawable -> drawable.draw(g));
    }
}
