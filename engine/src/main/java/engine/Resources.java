package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class Resources {
    private Resources() {
    }

    public static List<String> lines(String path) {
        var input = Resources.class.getResourceAsStream(path);
        if (input == null) {
            throw new IllegalStateException("No se encuentra el recurso " + path);
        }

        try (var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new IllegalStateException("Error en la carga del recurso " + path, e);
        }
    }
}
