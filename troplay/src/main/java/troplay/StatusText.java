package troplay;

import engine.Drawable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class StatusText extends Drawable {
    private static final Font FONT = new Font("LuciSans", Font.ITALIC, 20);
    private static final Color YELLOW = new Color(240, 240, 0);

    private String text = "";

    public StatusText() {
        setXY(754, 88);
        setLayer(Layer.UI);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g) {
        if (text.isEmpty()) {
            return;
        }

        g.setColor(YELLOW);
        g.setFont(FONT);
        g.drawString(text, x, y);
    }
}
