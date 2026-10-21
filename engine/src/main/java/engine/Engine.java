package engine;

import engine.input.Keyboard;
import engine.input.Mouse;

public class Engine {
    /** Duration of each update of the game: 40 updates per second */
    public static final long TICK_MILLIS = 25;

    private final GameCanvas canvas;
    private final Mouse mouse;
    private final Keyboard keyboard;
    private Flow<?, ?> flow;
    private Screen screen;
    private volatile boolean running;

    public Engine(GameCanvas canvas) {
        this.canvas = canvas;
        mouse = new Mouse(canvas);
        keyboard = new Keyboard(canvas);
    }

    public void run(Flow<?, ?> flow) {
        this.flow = flow;
        flow.start(this);
        canvas.requestFocusInWindow();
        running = true;

        while (running) {
            var start = System.currentTimeMillis();

            screen.tick();
            canvas.repaint();

            var elapsed = System.currentTimeMillis() - start;
            try {
                Thread.sleep(Math.max(0, TICK_MILLIS - elapsed));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    void show(Screen next) {
        screen = next;
        canvas.setScene(next.getScene());
        mouse.clear();
        keyboard.clear();
    }

    void fire(Enum<?> event) {
        flow.fire(event);
    }

    boolean isShowing(Screen screen) {
        return this.screen == screen;
    }

    public void exit() {
        running = false;
    }

    /**
     * Number of ticks that last the given time, at least one, so that games state
     * their durations in milliseconds regardless of the tick
     */
    public static int ticks(long millis) {
        return (int) Math.max(1, Math.round((double) millis / TICK_MILLIS));
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }
}
