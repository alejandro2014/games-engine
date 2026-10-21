package engine.input;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mouse extends MouseAdapter {
    public enum Type { PRESSED, RELEASED, MOVED }

    public record Event(Type type, Point point) {
    }

    private final Queue<Event> events = new ConcurrentLinkedQueue<>();

    public Mouse(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            events.add(new Event(Type.PRESSED, e.getPoint()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            events.add(new Event(Type.RELEASED, e.getPoint()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        events.add(new Event(Type.MOVED, e.getPoint()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        events.add(new Event(Type.MOVED, e.getPoint()));
    }

    public Event poll() { return events.poll(); }
    public void clear() { events.clear(); }
}
