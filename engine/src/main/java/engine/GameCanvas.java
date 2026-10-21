package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class GameCanvas extends JPanel {
    private volatile Scene scene = new Scene();

    public GameCanvas(int width, int height) {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scene.draw((Graphics2D) g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        repaint();
    }
}
