package troplay;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionDeck {
    private final TroplayDatabase database;
    private final Map<Difficulty, Deque<Integer>> piles = new EnumMap<>(Difficulty.class);

    public QuestionDeck(TroplayDatabase database) throws SQLException {
        this.database = database;

        for (var difficulty : Difficulty.values()) {
            var ids = IntStream.range(0, database.countQuestions(difficulty.databaseLevel()))
                    .boxed()
                    .collect(Collectors.toList());
            Collections.shuffle(ids);
            piles.put(difficulty, new ArrayDeque<>(ids));
        }
    }

    public Question draw(Difficulty difficulty) throws SQLException {
        var pile = piles.get(difficulty);
        if (pile.isEmpty()) {
            throw new IllegalStateException("No quedan preguntas de dificultad " + difficulty);
        }

        return database.fetchQuestion(pile.pop(), difficulty.databaseLevel());
    }
}
