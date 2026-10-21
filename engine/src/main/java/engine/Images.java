package engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

public final class Images {
    private static final String EXTENSION = ".png";
    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
    private static String directory = "/";

    private Images() {
    }

    public static synchronized void setDirectory(String directory) {
        Images.directory = directory.endsWith("/") ? directory : directory + "/";
        CACHE.clear();
    }

    /**
     * Uses the graphics of a game, which by convention are in the resources
     * folder named after its package, e.g. /jantrix/graphics/ for jantrix.Jantrix
     */
    public static void useGraphicsOf(Class<?> game) {
        setDirectory("/" + game.getPackageName().replace('.', '/') + "/graphics/");
    }

    public static synchronized BufferedImage load(String name) {
        return CACHE.computeIfAbsent(name, Images::read);
    }

    public static BufferedImage[] load(String... names) {
        return Arrays.stream(names).map(Images::load).toArray(BufferedImage[]::new);
    }

    /**
     * Name of a graphic inside the folder of its entity, e.g. path("buttons", "start", "es", "normal")
     * is "buttons/start/es/normal.png"
     */
    public static String path(String... parts) {
        return String.join("/", parts) + EXTENSION;
    }

    /**
     * Names of the frames of an animation stored as folder/1.png, folder/2.png...
     */
    public static String[] numbered(String folder, int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(frame -> path(folder, String.valueOf(frame)))
                .toArray(String[]::new);
    }

    private static BufferedImage read(String name) {
        var resource = Images.class.getResource(directory + name);
        if (resource == null) {
            throw new IllegalStateException("No se encuentra el gráfico " + directory + name);
        }

        try {
            return ImageIO.read(resource);
        } catch (IOException e) {
            throw new IllegalStateException("Error en la carga del gráfico " + name, e);
        }
    }
}
