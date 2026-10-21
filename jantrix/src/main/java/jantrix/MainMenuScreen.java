package jantrix;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainMenuScreen extends Screen {
    private static final int CENTER_X = 184;
    private static final int NUM_LEVELS = 10;

    private final Settings settings;
    private final List<MenuOption> options;
    private final MenuOption levelOption;
    private int selected;

    public MainMenuScreen(Engine engine, Settings settings) {
        super(engine);
        this.settings = settings;

        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "menu/background.png"));

        levelOption = new MenuOption(CENTER_X, 250, () -> "Nivel: " + (settings.getStartLevel() + 1),
                                     () -> changeLevel(1), this::select);
        options = List.of(
                new MenuOption(CENTER_X, 200, () -> "Empezar juego", () -> fire(AppEvent.START), this::select),
                levelOption,
                new MenuOption(CENTER_X, 300, () -> "Salir", () -> fire(AppEvent.EXIT), this::select));
        options.forEach(this::addWidget);
        select(0);
    }

    @Override
    protected void onKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> select((selected + options.size() - 1) % options.size());
            case KeyEvent.VK_DOWN -> select((selected + 1) % options.size());
            case KeyEvent.VK_LEFT -> changeLevelIfSelected(-1);
            case KeyEvent.VK_RIGHT -> changeLevelIfSelected(1);
            case KeyEvent.VK_ENTER -> options.get(selected).activate();
            default -> {
            }
        }
    }

    private void select(MenuOption option) {
        select(options.indexOf(option));
    }

    private void select(int index) {
        selected = index;
        for (var i = 0; i < options.size(); i++) {
            options.get(i).setSelected(i == index);
        }
    }

    private void changeLevelIfSelected(int delta) {
        if (options.get(selected) == levelOption) {
            changeLevel(delta);
        }
    }

    private void changeLevel(int delta) {
        settings.setStartLevel((settings.getStartLevel() + delta + NUM_LEVELS) % NUM_LEVELS);
    }
}
