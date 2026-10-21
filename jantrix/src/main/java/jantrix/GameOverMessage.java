package jantrix;

import engine.Drawable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GameOverMessage extends Drawable {
    private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 22);
    private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    private static final Color SHADE = new Color(0, 0, 0, 190);
    private static final Color YELLOW = new Color(200, 200, 40);
    private static final int WIDTH = Board.COLUMNS * Board.CELL_SIZE;

    public GameOverMessage() {
        setXY(16, 180);
        setLayer(Layer.OVERLAY);
        setVisible(false);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(SHADE);
        g.fillRect(x, y, WIDTH, 110);

        g.setColor(YELLOW);
        drawCentered(g, TITLE_FONT, "¡Juego terminado!", y + 40);
        g.setColor(Color.WHITE);
        drawCentered(g, TEXT_FONT, "Siempre puedes echar otra partida...", y + 70);
        drawCentered(g, TEXT_FONT, "Pulsa Intro para volver al menú", y + 92);
    }

    private void drawCentered(Graphics2D g, Font font, String text, int baseY) {
        g.setFont(font);
        g.drawString(text, x + (WIDTH - g.getFontMetrics().stringWidth(text)) / 2, baseY);
    }
}
