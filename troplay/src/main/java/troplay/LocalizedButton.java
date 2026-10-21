package troplay;

import engine.Images;
import engine.ui.Button;
import java.awt.Point;
import java.util.Arrays;
import java.util.stream.Stream;

public class LocalizedButton extends Button implements Localizable {
    private Language language;

    public LocalizedButton(String name, Point coords, Language language, Runnable action) {
        super(coords, Layer.UI, action, frameNames(name));
        setLanguage(language);
    }

    private static String[] frameNames(String name) {
        return Arrays.stream(Language.values())
                .flatMap(language -> Stream.of(Images.path("buttons", name, language.code(), "normal"),
                                                   Images.path("buttons", name, language.code(), "pressed")))
                .toArray(String[]::new);
    }

    @Override
    public void setLanguage(Language language) {
        this.language = language;
        refresh();
    }

    @Override
    protected int frameIndex(boolean pressed) {
        return 2 * language.ordinal() + (pressed ? 1 : 0);
    }
}
