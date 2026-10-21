package engine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class CheckBoxGroup {
    private final List<CheckBox> boxes = new ArrayList<>();
    private IntConsumer listener;

    void add(CheckBox box) {
        boxes.add(box);
        box.setChecked(boxes.size() == 1);
    }

    public void select(int index) {
        select(boxes.get(index));
    }

    void select(CheckBox selected) {
        boxes.forEach(box -> box.setChecked(box == selected));
        if (listener != null) {
            listener.accept(boxes.indexOf(selected));
        }
    }

    public int getSelected() {
        return IntStream.range(0, boxes.size())
                .filter(i -> boxes.get(i).isChecked())
                .findFirst()
                .orElse(-1);
    }

    public List<CheckBox> getBoxes() { return boxes; }
    public void setListener(IntConsumer listener) { this.listener = listener; }
}
