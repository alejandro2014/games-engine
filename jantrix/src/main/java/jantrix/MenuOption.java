package jantrix;

import engine.Drawable;
import engine.input.MouseTarget;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuOption extends Drawable implements MouseTarget {
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 24);
    private static final Color SELECTED = new Color(200, 200, 200);
    private static final Color NOT_SELECTED = new Color(200, 200, 40);

    private final Supplier<String> text;
    private final Runnable action;
    private final Consumer<MenuOption> onHover;
    private Rectangle bounds = new Rectangle();
    private boolean selected;

    /**
     * @param centerX horizontal center of the text
     * @param baseY baseline of the text
     */
    public MenuOption(int centerX, int baseY, Supplier<String> text, Runnable action, Consumer<MenuOption> onHover) {
        setXY(centerX, baseY);
        setLayer(Layer.HUD);
        this.text = text;
        this.action = action;
        this.onHover = onHover;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void activate() {
        action.run();
    }

    @Override
    public void draw(Graphics2D g) {
        var label = text.get();
        g.setFont(FONT);
        g.setColor(selected ? SELECTED : NOT_SELECTED);

        var metrics = g.getFontMetrics();
        var left = x - metrics.stringWidth(label) / 2;
        g.drawString(label, left, y);
        bounds = new Rectangle(left, y - metrics.getAscent(), metrics.stringWidth(label), metrics.getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onEnter() {
        onHover.accept(this);
    }

    @Override
    public void onClick(Point point) {
        activate();
    }
}
