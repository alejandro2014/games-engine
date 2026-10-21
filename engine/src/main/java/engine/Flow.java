package engine;

import java.lang.System.Logger.Level;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * State machine that drives the screens of a game. Each state shows a screen,
 * created anew every time the state is entered, and the screens fire events
 * that move the flow to the next state as declared in its transitions.
 * Entering a final state ends the game.
 * @param <S> states of the flow
 * @param <E> events fired by the screens
 */
public final class Flow<S extends Enum<S>, E extends Enum<E>> {
    private static final System.Logger LOG = System.getLogger(Flow.class.getName());

    @FunctionalInterface
    public interface ScreenFactory {
        Screen create() throws Exception;
    }

    private final S initial;
    private final Map<S, ScreenFactory> screens;
    private final Map<S, Map<E, S>> transitions;
    private final Set<S> finals;
    private Engine engine;
    private S state;

    private Flow(Builder<S, E> builder) {
        initial = builder.initial;
        screens = builder.screens;
        transitions = builder.transitions;
        finals = builder.finals;
    }

    public static <S extends Enum<S>, E extends Enum<E>> Builder<S, E> builder(Class<S> states, S initial) {
        return new Builder<>(states, initial);
    }

    public S getState() {
        return state;
    }

    void start(Engine engine) {
        this.engine = engine;
        enter(initial);
    }

    void fire(Enum<?> event) {
        var next = transitions.getOrDefault(state, Map.of()).get(event);
        if (next == null) {
            throw new IllegalStateException("No transition from " + state + " on " + event);
        }

        enter(next);
    }

    private void enter(S next) {
        if (finals.contains(next)) {
            state = next;
            engine.exit();
            return;
        }

        try {
            var screen = screens.get(next).create();
            state = next;
            engine.show(screen);
        } catch (Exception e) {
            if (state == null) {
                throw new IllegalStateException("Cannot start the flow in " + next, e);
            }
            LOG.log(Level.ERROR, "Cannot enter " + next + ", staying in " + state, e);
        }
    }

    public static final class Builder<S extends Enum<S>, E extends Enum<E>> {
        private final S initial;
        private final Map<S, ScreenFactory> screens;
        private final Map<S, Map<E, S>> transitions;
        private final Set<S> finals;

        private Builder(Class<S> states, S initial) {
            this.initial = initial;
            screens = new EnumMap<>(states);
            transitions = new EnumMap<>(states);
            finals = EnumSet.noneOf(states);
        }

        public Builder<S, E> screen(S state, ScreenFactory factory) {
            screens.put(state, factory);
            return this;
        }

        public Builder<S, E> on(S from, E event, S to) {
            transitions.computeIfAbsent(from, state -> new HashMap<>()).put(event, to);
            return this;
        }

        public Builder<S, E> end(S state) {
            finals.add(state);
            return this;
        }

        public Flow<S, E> build() {
            requireScreen(initial);
            transitions.forEach((from, events) -> {
                requireScreen(from);
                events.values().stream().filter(to -> !finals.contains(to)).forEach(this::requireScreen);
            });
            return new Flow<>(this);
        }

        private void requireScreen(S state) {
            if (!screens.containsKey(state)) {
                throw new IllegalStateException("No screen for state " + state);
            }
        }
    }
}
