package troplay;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import java.awt.Point;

public class MainMenuScreen extends Screen {
    public MainMenuScreen(Engine engine, Settings settings) {
        super(engine);
        var language = settings.getLanguage();

        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "presentation/background.png"));

        addWidget(new LocalizedButton("start", new Point(389, 234), language, () -> fire(AppEvent.START)));
        addWidget(new LocalizedButton("options", new Point(389, 303), language, () -> fire(AppEvent.OPTIONS)));
        addWidget(new LocalizedButton("exit", new Point(389, 372), language, () -> fire(AppEvent.EXIT)));
    }
}
