package jantrix;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import java.awt.Point;

public class SplashScreen extends Screen {
    private static final int DURATION_TICKS = Engine.ticks(2_000);

    private int ticks;

    public SplashScreen(Engine engine) {
        super(engine);
        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "splash/background.png"));
    }

    @Override
    protected void update() {
        if (++ticks >= DURATION_TICKS) {
            showMenu();
        }
    }

    @Override
    protected void onKey(int keyCode) {
        showMenu();
    }

    private void showMenu() {
        fire(AppEvent.SPLASH_DONE);
    }
}
