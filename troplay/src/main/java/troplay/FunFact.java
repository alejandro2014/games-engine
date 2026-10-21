package troplay;

import engine.Drawable;
import engine.Images;
import engine.text.RichText;
import engine.text.TextWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

public class FunFact extends Drawable {
    private static final Font FONT = new Font("Lucida Bright", Font.PLAIN, 18);
    private static final Font SMALL_FONT = new Font("Lucida Bright", Font.PLAIN, 14);
    private static final Map<RichText.Style, Font> FONTS = Map.of(
            RichText.Style.NORMAL, FONT,
            RichText.Style.ITALIC, new Font("Lucida Bright", Font.ITALIC, 18),
            RichText.Style.SUBSCRIPT, SMALL_FONT,
            RichText.Style.SUPERSCRIPT, SMALL_FONT);
    private static final Color YELLOW = new Color(240, 240, 0);
    private static final int LINE_WIDTH = 44;
    private static final int LINE_HEIGHT = 25;

    private final Question text;
    private final Image title;

    public FunFact(Question text, Language language) {
        this.text = text;
        currentImg = Images.load("fun-fact/background.png");
        title = Images.load(Images.path("fun-fact", "title", language.code()));
        setXY(100, 100);
        setLayer(Layer.OVERLAY);
        setVisible(false);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.drawImage(title, x + 10, y + 5, null);

        var writer = new TextWriter(LINE_WIDTH, LINE_HEIGHT);
        writer.newParagraph(x + 25);
        g.setColor(YELLOW);
        writer.write(g, text.getQuestionChunks(), FONTS, y + 90, 5);
    }
}
