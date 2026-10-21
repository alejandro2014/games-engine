package engine.text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

public class TextWriter {
    private final int lineWidth;
    private final int lineHeight;
    private int lineStartX;
    private int cursorX;
    private int remaining;
    private int currentLine;

    public TextWriter(int lineWidth, int lineHeight) {
        this.lineWidth = lineWidth;
        this.lineHeight = lineHeight;
    }

    public void newParagraph(int x) {
        lineStartX = x;
        cursorX = x;
        remaining = lineWidth;
    }

    public void newLine() {
        currentLine++;
    }

    public void write(Graphics2D g, List<RichText.Chunk> chunks, Map<RichText.Style, Font> fonts,
                      int baseY, int scriptOffset) {
        for (var chunk : chunks) {
            var font = fonts.get(chunk.style());
            var offset = switch (chunk.style()) {
                case SUBSCRIPT -> scriptOffset;
                case SUPERSCRIPT -> -scriptOffset;
                default -> 0;
            };

            g.setFont(font);
            writeChunk(g, chunk.text(), baseY + offset, g.getFontMetrics(font));
        }
    }

    private void writeChunk(Graphics2D g, String text, int baseY, FontMetrics metrics) {
        while (!text.isEmpty()) {
            var end = Math.min(text.length(), remaining);
            g.drawString(text.substring(0, end), cursorX, currentLine * lineHeight + baseY);

            if (text.length() <= remaining) {
                remaining -= text.length();
                cursorX += metrics.stringWidth(text);
                text = "";
            } else {
                cursorX = lineStartX;
                text = text.substring(remaining);
                remaining = lineWidth;
                currentLine++;
            }
        }
    }
}
