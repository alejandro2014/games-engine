package engine;

/**
 * Base of every game. A game extends it, declares its window and its flow, and its
 * main method just calls {@link #launch()}. By convention, the graphics of the game
 * are in the graphics folder of its package (see {@link Images#useGraphicsOf}),
 * and icon.png in that folder is the icon of the window.
 */
public abstract class GameApp {
    private final String title;
    private final int width;
    private final int height;

    protected GameApp(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    /**
     * The screens of the game and how it moves between them
     */
    protected abstract Flow<?, ?> flow(Engine engine);

    public final void launch() {
        Images.useGraphicsOf(getClass());

        var window = new GameWindow(title, width, height);
        window.setVisible(true);

        var engine = new Engine(window.getCanvas());
        engine.run(flow(engine));
        System.exit(0);
    }
}
