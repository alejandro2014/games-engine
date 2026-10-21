package troplay;

import engine.data.Database;
import java.sql.SQLException;
import java.util.Random;

public class TroplayDatabase implements AutoCloseable {
    private static final String RESOURCE = "troplay/db/tenia.sqlite";

    private record Answer(String text, boolean correct) {
    }

    private final Database database;
    private final int language;
    private final Random random = new Random();

    public TroplayDatabase(Language language) throws SQLException {
        database = Database.sqliteResource(RESOURCE);
        this.language = language.databaseId();
    }

    public int countQuestions(int difficulty) throws SQLException {
        return database.queryInt(
                "select count(*) from respuesta where dificultad = ? and id_idioma = ?",
                difficulty, language) / 3;
    }

    public Question fetchQuestion(int index, int difficulty) throws SQLException {
        var questionId = database.queryFirst(
                "select distinct id_pregunta from respuesta where id_idioma = ? and dificultad = ? limit 1 offset ?",
                row -> row.getInt("id_pregunta"),
                language, difficulty, index).orElse(0);

        var answers = database.query(
                "select respuesta, correcta from respuesta where id_pregunta = ?",
                row -> new Answer(row.getString("respuesta"), row.getString("correcta").equals("true")),
                questionId);

        var question = new Question();
        for (var i = 0; i < answers.size(); i++) {
            question.setAnswerText(i, answers.get(i).text());
            if (answers.get(i).correct()) {
                question.setCorrectAnswer(i);
            }
        }

        database.queryFirst(
                "select enunciado from preguntas where id_pregunta = ?",
                row -> row.getString("enunciado"),
                questionId).ifPresent(question::setQuestionText);

        return question;
    }

    public String getFunFactText() throws SQLException {
        var count = database.queryInt("select count(*) from curiosidadtropical where id_idioma = ?", language);

        return database.queryFirst(
                "SELECT curiosidad from curiosidadtropical where id_idioma = ? limit 1 offset ?",
                row -> row.getString("curiosidad"),
                language, random.nextInt(count)).orElseThrow();
    }

    @Override
    public void close() throws SQLException {
        database.close();
    }
}
