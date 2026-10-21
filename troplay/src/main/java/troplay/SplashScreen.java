package troplay;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import java.awt.Point;

public class SplashScreen extends Screen {
    private static final int DURATION_TICKS = Engine.ticks(4_200);

    private int ticks;

    public SplashScreen(Engine engine) {
        super(engine);
        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "presentation/background.png"));
    }

    @Override
    protected void update() {
        if (++ticks >= DURATION_TICKS) {
            fire(AppEvent.SPLASH_DONE);
        }
    }
}
