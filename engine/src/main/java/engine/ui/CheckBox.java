package engine.ui;

import engine.Sprite;
import engine.input.MouseTarget;
import java.awt.Point;

public class CheckBox extends Sprite implements MouseTarget {
    private final CheckBoxGroup group;
    private boolean checked;

    public CheckBox(CheckBoxGroup group, Point coords, Enum<?> layer, String unchecked, String checked) {
        super(coords, layer, unchecked, checked);
        this.group = group;
        group.add(this);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        setFrame(checked ? 1 : 0);
    }

    @Override
    public void onPress(Point point) {
        group.select(this);
    }
}
