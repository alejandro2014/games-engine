package troplay;

import engine.Images;
import engine.Sprite;
import java.awt.Point;
import java.util.Arrays;

public class LocalizedSprite extends Sprite implements Localizable {
    public LocalizedSprite(String folder, Point coords, Enum<?> layer, Language language) {
        super(coords, layer, frameNames(folder));
        setLanguage(language);
    }

    private static String[] frameNames(String folder) {
        return Arrays.stream(Language.values())
                .map(language -> Images.path(folder, language.code()))
                .toArray(String[]::new);
    }

    @Override
    public void setLanguage(Language language) {
        setFrame(language.ordinal());
    }
}
