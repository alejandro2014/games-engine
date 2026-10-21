package troplay;

import engine.Engine;
import engine.Flow;
import engine.GameApp;

public final class Troplay extends GameApp {
    private Troplay() {
        super("TROPLAY - El juego de la tenia", 946, 644);
    }

    public static void main(String[] args) {
        new Troplay().launch();
    }

    @Override
    protected Flow<AppState, AppEvent> flow(Engine engine) {
        var settings = new Settings();

        return Flow.<AppState, AppEvent>builder(AppState.class, AppState.SPLASH)
                .screen(AppState.SPLASH, () -> new SplashScreen(engine))
                .screen(AppState.MENU, () -> new MainMenuScreen(engine, settings))
                .screen(AppState.OPTIONS, () -> new OptionsScreen(engine, settings))
                .screen(AppState.GAME, () -> new Game(engine, settings))
                .end(AppState.EXIT)
                .on(AppState.SPLASH, AppEvent.SPLASH_DONE, AppState.MENU)
                .on(AppState.MENU, AppEvent.START, AppState.GAME)
                .on(AppState.MENU, AppEvent.OPTIONS, AppState.OPTIONS)
                .on(AppState.MENU, AppEvent.EXIT, AppState.EXIT)
                .on(AppState.OPTIONS, AppEvent.BACK, AppState.MENU)
                .on(AppState.GAME, AppEvent.BACK, AppState.MENU)
                .build();
    }
}
