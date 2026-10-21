package troplay;

import engine.text.RichText;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private static final int NUM_ANSWERS = 3;

    private final String[] answers = new String[NUM_ANSWERS];
    private final List<List<RichText.Chunk>> answerChunks = new ArrayList<>(List.of(List.of(), List.of(), List.of()));
    private String questionText;
    private List<RichText.Chunk> questionChunks = List.of();
    private int correctAnswer = -1;

    public String getQuestionText() { return questionText; }
    public List<RichText.Chunk> getQuestionChunks() { return questionChunks; }
    public List<RichText.Chunk> getAnswerChunks(int answer) { return answerChunks.get(answer); }

    public int getQuestionLines(int lineWidth) { return RichText.countLines(questionText, lineWidth); }
    public int getAnswerLines(int answer, int lineWidth) { return RichText.countLines(answers[answer], lineWidth); }

    public boolean isCorrect(int option) { return option == correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }

    public void setQuestionText(String text) {
        questionText = text;
        questionChunks = RichText.parse(text);
    }

    public void setAnswerText(int answer, String text) {
        answers[answer] = text.substring(0, 1).toUpperCase() + text.substring(1);
        answerChunks.set(answer, RichText.parse(answers[answer]));
    }
}
