package engine;

import engine.input.InputDispatcher;
import engine.input.MouseTarget;

public abstract class Screen {
    protected final Engine engine;
    protected final Scene scene = new Scene();
    protected final InputDispatcher input = new InputDispatcher();

    protected Screen(Engine engine) {
        this.engine = engine;
    }

    public final void tick() {
        input.dispatch(engine.getMouse());
        for (var key = engine.getKeyboard().poll(); key != null && isShowing(); key = engine.getKeyboard().poll()) {
            onKey(key);
        }
        if (isShowing()) {
            update();
            scene.update();
        }
    }

    protected void update() {
    }

    /**
     * Tells the flow that something happened in this screen, which may lead to another screen.
     * Events fired once the screen has been left are ignored.
     */
    protected void fire(Enum<?> event) {
        if (isShowing()) {
            engine.fire(event);
        }
    }

    private boolean isShowing() {
        return engine.isShowing(this);
    }

    protected void onKey(int keyCode) {
    }

    public Scene getScene() {
        return scene;
    }

    protected <T extends Drawable & MouseTarget> T addWidget(T widget) {
        scene.add(widget);
        input.add(widget);
        return widget;
    }

    protected void removeWidget(Drawable widget) {
        scene.remove(widget);
        if (widget instanceof MouseTarget target) {
            input.remove(target);
        }
    }
}
