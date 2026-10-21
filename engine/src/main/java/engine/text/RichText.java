package engine.text;

import java.util.ArrayList;
import java.util.List;

public final class RichText {
    public enum Style {
        NORMAL, ITALIC, SUBSCRIPT, SUPERSCRIPT;

        static Style fromMarker(char marker) {
            return switch (marker) {
                case '$' -> ITALIC;
                case '#' -> SUBSCRIPT;
                case '@' -> SUPERSCRIPT;
                default -> null;
            };
        }
    }

    public record Chunk(String text, Style style) {
    }

    private RichText() {
    }

    public static List<Chunk> parse(String text) {
        var chunks = new ArrayList<Chunk>();
        var style = Style.NORMAL;
        var start = 0;

        var opening = Style.fromMarker(text.charAt(0));
        if (opening != null) {
            style = opening;
            start = 1;
        }

        for (var i = 1; i < text.length(); i++) {
            var marker = Style.fromMarker(text.charAt(i));
            if (marker == null) {
                continue;
            }

            chunks.add(new Chunk(text.substring(start, i), style));
            style = style == Style.NORMAL ? marker : Style.NORMAL;
            start = i + 1;
        }

        chunks.add(new Chunk(text.substring(start), style));
        return List.copyOf(chunks);
    }

    public static int countLines(String text, int lineWidth) {
        return (text.length() + lineWidth - 1) / lineWidth;
    }
}
