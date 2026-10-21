package troplay;

import engine.Engine;
import engine.Screen;
import engine.Sprite;
import engine.ui.CheckBoxGroup;
import java.awt.Point;
import java.util.List;

public class OptionsScreen extends Screen {
    private final Settings settings;
    private final List<Localizable> localized;

    public OptionsScreen(Engine engine, Settings settings) {
        super(engine);
        this.settings = settings;
        var language = settings.getLanguage();

        scene.setBackground(new Sprite(new Point(), Layer.BACKGROUND, "options/background.png"));

        localized = List.of(
                addWidget(new LocalizedButton("back-to-menu", new Point(574, 220), language,
                        () -> fire(AppEvent.BACK))),
                addLabel("options/labels/title", new Point(310, 66), language),
                addLabel("options/labels/language", new Point(320, 161), language),
                addLabel("options/labels/players", new Point(318, 299), language));

        var languages = new CheckBoxGroup();
        addWidget(new TroplayCheckBox(languages, new Point(346, 226)));
        addWidget(new TroplayCheckBox(languages, new Point(513, 225)));
        languages.select(language.ordinal());
        languages.setListener(index -> setLanguage(Language.values()[index]));

        var players = new CheckBoxGroup();
        addWidget(new TroplayCheckBox(players, new Point(317, 384)));
        addWidget(new TroplayCheckBox(players, new Point(476, 384)));
        addWidget(new TroplayCheckBox(players, new Point(317, 484)));
        addWidget(new TroplayCheckBox(players, new Point(480, 485)));
        players.select(settings.getNumPlayers() - 1);
        players.setListener(index -> settings.setNumPlayers(index + 1));
    }

    private LocalizedSprite addLabel(String folder, Point coords, Language language) {
        var label = new LocalizedSprite(folder, coords, Layer.UI, language);
        scene.add(label);
        return label;
    }

    private void setLanguage(Language language) {
        settings.setLanguage(language);
        localized.forEach(item -> item.setLanguage(language));
    }
}
