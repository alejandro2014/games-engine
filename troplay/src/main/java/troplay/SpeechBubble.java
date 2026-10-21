package troplay;

import engine.Drawable;
import engine.text.RichText;
import engine.text.TextWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.stream.IntStream;

public class SpeechBubble extends Drawable {
    public enum Direction { UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT }

    private static final Font QUESTION_FONT = new Font("LuciSans", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("LuciSans", Font.PLAIN, 10);
    private static final Map<RichText.Style, Font> FONTS = Map.of(
            RichText.Style.NORMAL, QUESTION_FONT,
            RichText.Style.ITALIC, new Font("LuciSans", Font.ITALIC, 14),
            RichText.Style.SUBSCRIPT, SMALL_FONT,
            RichText.Style.SUPERSCRIPT, SMALL_FONT);

    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color BLACK = new Color(0, 0, 0);

    private static final int ANSWERS_X = 726;
    private static final int QUESTION_WIDTH = 40;
    private static final int ANSWER_WIDTH = 28;
    private static final int NUM_ANSWERS = 3;
    private static final int[] ANSWER_OFFSETS = {4, 3, 3, 2, 2, 1, 1, 0, 0, -1};
    private static final int DISTANCE = 50;
    private static final int LINE_HEIGHT = 25;

    private Question question;
    private Square square;

    public SpeechBubble() {
        setLayer(Layer.BUBBLE);
        setVisible(false);
    }

    public void show(Question question, Square square) {
        this.question = question;
        this.square = square;
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
        question = null;
    }

    public int getCheckBoxY(int answer) {
        var previousLines = IntStream.range(0, answer)
                .map(i -> question.getAnswerLines(i, ANSWER_WIDTH))
                .sum();

        return previousLines * 25 + getAnswersY() - 13;
    }

    private int getAnswersY() {
        var numLines = IntStream.range(0, NUM_ANSWERS)
                .map(i -> question.getAnswerLines(i, ANSWER_WIDTH))
                .sum();

        return 417 - (numLines - 1 + ANSWER_OFFSETS[numLines - 3]) * 25;
    }

    @Override
    public void draw(Graphics2D g) {
        var current = question;
        if (current == null) {
            return;
        }

        drawBubble(g, current);
        writeAnswers(g, current);
    }

    private void drawBubble(Graphics2D g, Question current) {
        var tipX = square.getX() + 4;
        var tipY = square.getY() + 24;
        var numLines = current.getQuestionLines(QUESTION_WIDTH);
        var text = current.getQuestionText();

        var width = g.getFontMetrics(QUESTION_FONT).stringWidth(text.substring(0, Math.min(text.length(), QUESTION_WIDTH))) + 20;
        var height = numLines * 16 + (numLines - 1) * 7 + 20;

        int bubbleX = 0, bubbleY = 0;
        int[] xs = {0, 0, tipX};
        int[] ys = {0, 0, tipY};

        switch (square.getBubbleDirection()) {
            case UP_LEFT -> {
                bubbleX = tipX - DISTANCE - width;
                bubbleY = tipY - DISTANCE - height;
                xs[0] = bubbleX + width;
                xs[1] = bubbleX + 2 * width / 3;
                ys[0] = bubbleY + 2 * height / 3;
                ys[1] = bubbleY + height;
            }
            case UP_RIGHT -> {
                bubbleX = tipX + DISTANCE;
                bubbleY = tipY - DISTANCE - height;
                xs[0] = bubbleX;
                xs[1] = bubbleX + width / 3;
                ys[0] = bubbleY + 2 * height / 3;
                ys[1] = bubbleY + height;
            }
            case DOWN_RIGHT -> {
                bubbleX = tipX + DISTANCE;
                bubbleY = tipY + DISTANCE;
                xs[0] = bubbleX + width / 3;
                xs[1] = bubbleX;
                ys[0] = bubbleY;
                ys[1] = bubbleY + height / 3;
            }
            case DOWN_LEFT -> {
                bubbleX = tipX - DISTANCE - width;
                bubbleY = tipY + DISTANCE;
                xs[0] = bubbleX + width;
                xs[1] = bubbleX + 2 * width / 3;
                ys[0] = bubbleY + height / 3;
                ys[1] = bubbleY;
            }
            case UP -> {
                bubbleX = tipX - DISTANCE;
                bubbleY = tipY - DISTANCE - height;
                xs[0] = bubbleX + width / 3;
                xs[1] = bubbleX + 2 * width / 3;
                ys[0] = bubbleY + height;
                ys[1] = bubbleY + height;
            }
            case RIGHT -> {
                bubbleX = tipX + DISTANCE;
                bubbleY = tipY - DISTANCE;
                xs[0] = bubbleX;
                xs[1] = bubbleX;
                ys[0] = bubbleY + height / 3;
                ys[1] = bubbleY + 2 * height / 3;
            }
            case DOWN -> {
                bubbleX = tipX - DISTANCE;
                bubbleY = tipY + DISTANCE;
                xs[0] = bubbleX + width / 3;
                xs[1] = bubbleX + 2 * width / 3;
                ys[0] = bubbleY;
                ys[1] = bubbleY;
            }
            case LEFT -> {
                bubbleX = tipX - DISTANCE - width;
                bubbleY = tipY - DISTANCE;
                xs[0] = bubbleX + width;
                xs[1] = bubbleX + width;
                ys[0] = bubbleY + height / 3;
                ys[1] = bubbleY + 2 * height / 3;
            }
        }

        var rect = new RoundRectangle2D.Double(bubbleX, bubbleY, width, height, 10, 10);
        var triangle = new Polygon(xs, ys, 3);

        g.setColor(WHITE);
        g.fill(rect);
        g.setColor(BLACK);
        g.draw(rect);

        g.setColor(WHITE);
        g.fill(triangle);
        g.setColor(BLACK);
        g.drawLine(xs[0], ys[0], xs[2], ys[2]);
        g.drawLine(xs[1], ys[1], xs[2], ys[2]);

        var writer = new TextWriter(QUESTION_WIDTH, LINE_HEIGHT);
        writer.newParagraph(bubbleX + 10);
        writer.write(g, current.getQuestionChunks(), FONTS, bubbleY + 25, 4);
    }

    private void writeAnswers(Graphics2D g, Question current) {
        var writer = new TextWriter(ANSWER_WIDTH, LINE_HEIGHT);
        var baseY = getAnswersY();

        g.setColor(BLACK);
        for (var answer = 0; answer < NUM_ANSWERS; answer++) {
            writer.newParagraph(ANSWERS_X);
            writer.write(g, current.getAnswerChunks(answer), FONTS, baseY, 4);
            writer.newLine();
        }
    }
}
