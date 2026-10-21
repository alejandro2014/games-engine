package jantrix;

import engine.Drawable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Level, score and lines, written under their labels of the background
 */
public class Hud extends Drawable {
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final int LEVEL_Y = 235;
    private static final int SCORE_Y = 315;
    private static final int LINES_Y = 405;

    private int level;
    private int score;
    private int lines;

    public Hud() {
        setXY(303, 0);
        setLayer(Layer.HUD);
    }

    public void set(int level, int score, int lines) {
        this.level = level;
        this.score = score;
        this.lines = lines;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(FONT);
        drawCentered(g, String.valueOf(level + 1), LEVEL_Y);
        drawCentered(g, String.valueOf(score), SCORE_Y);
        drawCentered(g, String.valueOf(lines), LINES_Y);
    }

    private void drawCentered(Graphics2D g, String text, int baseY) {
        g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, baseY);
    }
}
