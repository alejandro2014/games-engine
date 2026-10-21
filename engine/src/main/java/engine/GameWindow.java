package engine;

import java.lang.System.Logger.Level;
import javax.swing.JFrame;

/**
 * Window of a game. Its icon is the graphic icon.png of the images directory,
 * so the directory must be set before creating the window.
 */
public class GameWindow extends JFrame {
    private static final System.Logger LOG = System.getLogger(GameWindow.class.getName());
    private static final String ICON = "icon.png";

    private final GameCanvas canvas;

    public GameWindow(String title, int width, int height) {
        canvas = new GameCanvas(width, height);
        add(canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        setResizable(false);
        loadIcon();
        pack();
        setLocationRelativeTo(null);
    }

    private void loadIcon() {
        try {
            setIconImage(Images.load(ICON));
        } catch (IllegalStateException e) {
            LOG.log(Level.WARNING, "The game has no icon: {0}", e.getMessage());
        }
    }

    public GameCanvas getCanvas() {
        return canvas;
    }
}
