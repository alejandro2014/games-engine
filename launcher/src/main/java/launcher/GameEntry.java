package launcher;

import java.util.function.Consumer;

/**
 * A game that can be started from the launcher
 * @param title name shown in the launcher
 * @param main entry point of the game, it receives the command line arguments
 */
public record GameEntry(String title, Consumer<String[]> main) {
}
