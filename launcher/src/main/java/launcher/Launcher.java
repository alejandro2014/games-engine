package launcher;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public final class Launcher {
    // To add a new game, register its entry point here
    private static final List<GameEntry> GAMES = List.of(
            new GameEntry("Troplay", troplay.Troplay::main),
            new GameEntry("Jantrix", jantrix.Jantrix::main));

    private Launcher() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> show(args));
    }

    private static void show(String[] args) {
        var frame = new JFrame("Juegos");

        var title = new JLabel("Elige un juego", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        var buttons = new JPanel(new GridLayout(0, 1, 0, 8));
        for (var game : GAMES) {
            var button = new JButton(game.title());
            button.addActionListener(e -> start(frame, game, args));
            buttons.add(button);
        }

        var content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 24, 40));
        content.add(title, BorderLayout.NORTH);
        content.add(buttons, BorderLayout.CENTER);

        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void start(JFrame frame, GameEntry game, String[] args) {
        frame.dispose();
        // The games run their own loop, which must not block the Swing event thread
        new Thread(() -> game.main().accept(args), game.title()).start();
    }
}
