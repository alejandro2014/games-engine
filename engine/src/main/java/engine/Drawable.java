package engine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Drawable {
    protected int x;
    protected int y;
    protected int z;
    protected Image currentImg;
    protected boolean visible = true;

    public void update() {
    }

    public void draw(Graphics2D g) {
        if (currentImg != null) {
            g.drawImage(currentImg, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return currentImg == null
                ? new Rectangle(x, y, 0, 0)
                : new Rectangle(x, y, currentImg.getWidth(null), currentImg.getHeight(null));
    }

    public boolean contains(Point point) {
        return visible && getBounds().contains(point);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public boolean isVisible() { return visible; }

    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }
    public void setLayer(Enum<?> layer) { z = layer.ordinal(); }
    public void setVisible(boolean visible) { this.visible = visible; }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setCoords(Point coords) {
        setXY(coords.x, coords.y);
    }
}
