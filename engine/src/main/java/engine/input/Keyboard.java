package engine.input;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Keyboard extends KeyAdapter {
    private final Queue<Integer> pressed = new ConcurrentLinkedQueue<>();

    public Keyboard(Component component) {
        component.setFocusable(true);
        component.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyCode());
    }

    public Integer poll() { return pressed.poll(); }
    public void clear() { pressed.clear(); }
}
