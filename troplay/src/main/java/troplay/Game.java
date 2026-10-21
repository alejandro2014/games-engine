package troplay;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import engine.ui.CheckBoxGroup;
import java.awt.Point;
import java.sql.SQLException;

public class Game extends Screen {
    private static final int QUESTIONS_PER_SQUARE = 3;
    private static final int NUM_ANSWERS = 3;
    private static final int FUN_FACT_TICKS = Engine.ticks(14_000);

    private enum State { ASKING, ROLLING, MOVING, FUN_FACT }

    private final Language language;
    private final Board board;
    private final Player[] players;
    private final PlayerIndicator[] indicators;
    private final Dice dice = new Dice();
    private final SpeechBubble bubble = new SpeechBubble();
    private final StatusText statusText = new StatusText();
    private final CheckBoxGroup answers = new CheckBoxGroup();
    private final FunFact funFact;

    private State state;
    private int currentPlayer = -1;
    private Square currentSquare;
    private int funFactTicks;

    public Game(Engine engine, Settings settings) throws SQLException {
        super(engine);
        language = settings.getLanguage();

        var numPlayers = settings.getNumPlayers();
        board = new Board(numPlayers > 1);
        players = new Player[numPlayers];
        indicators = new PlayerIndicator[numPlayers];

        try (var database = new TroplayDatabase(language)) {
            dealQuestions(database);
            funFact = new FunFact(loadFunFact(database), language);
        }

        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "game/background.png"));
        scene.add(board);
        scene.add(dice);
        scene.add(statusText);
        scene.add(bubble);
        scene.add(funFact);

        addWidget(new LocalizedButton("answer", new Point(746, 470), language, this::answer));
        addWidget(new LocalizedButton("back", new Point(746, 512), language, this::quit));

        for (var i = 0; i < NUM_ANSWERS; i++) {
            addWidget(new TroplayCheckBox(answers, new Point(703, 0)));
        }
        showAnswers(false);

        for (var i = 0; i < numPlayers; i++) {
            players[i] = new Player(board, i);
            indicators[i] = new PlayerIndicator(i);
            scene.add(players[i]);
            scene.add(indicators[i]);
        }

        startTurn();
    }

    private void dealQuestions(TroplayDatabase database) throws SQLException {
        var deck = new QuestionDeck(database);

        for (var square : board.getSquares()) {
            if (square.getType() == Square.Type.GOAL) {
                continue;
            }
            for (var i = 0; i < QUESTIONS_PER_SQUARE; i++) {
                square.addQuestion(deck.draw(square.getDifficulty()));
            }
        }
    }

    private static Question loadFunFact(TroplayDatabase database) throws SQLException {
        var text = database.getFunFactText();
        var question = new Question();
        question.setQuestionText(text.substring(text.indexOf("?") + 1));
        return question;
    }

    @Override
    protected void update() {
        switch (state) {
            case ROLLING -> {
                if (!dice.isRolling()) {
                    player().advance(dice.getValue());
                    setState(State.MOVING);
                }
            }
            case MOVING -> {
                if (!player().isMoving()) {
                    endMove();
                }
            }
            case FUN_FACT -> {
                if (++funFactTicks >= FUN_FACT_TICKS) {
                    quit();
                }
            }
        }
    }

    private void setState(State state) {
        this.state = state;
        input.setEnabled(state == State.ASKING);
    }

    private Player player() {
        return players[currentPlayer];
    }

    private void startTurn() {
        currentPlayer = (currentPlayer + 1) % players.length;

        while (!player().canRoll()) {
            player().setCanRoll(true);
            currentPlayer = (currentPlayer + 1) % players.length;
        }

        for (var i = 0; i < players.length; i++) {
            indicators[i].setActive(i == currentPlayer);
        }

        currentSquare = board.getSquare(player().getSquare());
        bubble.show(currentSquare.getCurrentQuestion(), currentSquare);

        var boxes = answers.getBoxes();
        for (var i = 0; i < boxes.size(); i++) {
            boxes.get(i).setY(bubble.getCheckBoxY(i));
        }
        answers.select(0);
        showAnswers(true);

        setState(State.ASKING);
    }

    private void answer() {
        var playerNumber = currentPlayer + 1;
        var correct = currentSquare.getCurrentQuestion().isCorrect(answers.getSelected());

        hideQuestion();

        if (correct) {
            statusText.setText(language.correct(playerNumber));
            currentSquare.questionAnswered();
            dice.roll();
            setState(State.ROLLING);
        } else {
            statusText.setText(language.wrong(playerNumber));
            startTurn();
        }
    }

    private void endMove() {
        var square = player().getSquare();

        if (board.isGoal(square)) {
            statusText.setText(language.win(currentPlayer + 1));
            funFact.setVisible(true);
            setState(State.FUN_FACT);
            return;
        }

        if (board.isBrake(square)) {
            player().setCanRoll(false);
            statusText.setText(language.brake(currentPlayer + 1));
        }

        startTurn();
    }

    private void quit() {
        hideQuestion();
        fire(AppEvent.BACK);
    }

    private void hideQuestion() {
        bubble.hide();
        showAnswers(false);
    }

    private void showAnswers(boolean visible) {
        answers.getBoxes().forEach(box -> box.setVisible(visible));
    }
}
