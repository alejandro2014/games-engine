package troplay;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Square {
    public enum Type { NORMAL, LADDER, BRAKE, GOAL }

    private final Point position;
    private final SpeechBubble.Direction bubbleDirection;
    private final Difficulty difficulty;
    private final Type type;
    private final int link;
    private final List<Question> questions = new ArrayList<>();
    private int questionIndex;

    public Square(Point position, SpeechBubble.Direction bubbleDirection, Difficulty difficulty, Type type, int link) {
        this.position = position;
        this.bubbleDirection = bubbleDirection;
        this.difficulty = difficulty;
        this.type = type;
        this.link = link;
    }

    public Point getPosition() { return position; }
    public int getX() { return position.x; }
    public int getY() { return position.y; }
    public SpeechBubble.Direction getBubbleDirection() { return bubbleDirection; }
    public Difficulty getDifficulty() { return difficulty; }
    public Type getType() { return type; }
    public int getLink() { return link; }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Question getCurrentQuestion() {
        return questions.get(questionIndex);
    }

    public void questionAnswered() {
        questionIndex = (questionIndex + 1) % questions.size();
    }
}
