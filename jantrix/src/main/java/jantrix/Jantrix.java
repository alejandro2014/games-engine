package jantrix;

import engine.Engine;
import engine.Flow;
import engine.GameApp;

/**
 * Jantrix, a version of tetris, one of the most well-known games
 * Started: 22/10/2008
 * @author Alejandro Rupérez Recio
 */
public final class Jantrix extends GameApp {
    private Jantrix() {
        super("Jantrix", 369, 492);
    }

    public static void main(String[] args) {
        new Jantrix().launch();
    }

    @Override
    protected Flow<AppState, AppEvent> flow(Engine engine) {
        var settings = new Settings();

        return Flow.<AppState, AppEvent>builder(AppState.class, AppState.SPLASH)
                .screen(AppState.SPLASH, () -> new SplashScreen(engine))
                .screen(AppState.MENU, () -> new MainMenuScreen(engine, settings))
                .screen(AppState.GAME, () -> new Game(engine, settings.getStartLevel()))
                .end(AppState.EXIT)
                .on(AppState.SPLASH, AppEvent.SPLASH_DONE, AppState.MENU)
                .on(AppState.MENU, AppEvent.START, AppState.GAME)
                .on(AppState.MENU, AppEvent.EXIT, AppState.EXIT)
                .on(AppState.GAME, AppEvent.BACK, AppState.MENU)
                .build();
    }
}
