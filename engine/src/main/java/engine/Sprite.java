package engine;

import java.awt.Image;
import java.awt.Point;

public class Sprite extends Drawable {
    private final Image[] frames;

    public Sprite(Point coords, Enum<?> layer, String... names) {
        frames = Images.load(names);
        setCoords(coords);
        setLayer(layer);
        setFrame(0);
    }

    public void setFrame(int frame) {
        currentImg = frames[frame];
    }

    public int getFrameCount() {
        return frames.length;
    }
}
