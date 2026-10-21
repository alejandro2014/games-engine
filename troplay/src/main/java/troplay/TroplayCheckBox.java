package troplay;

import engine.ui.CheckBox;
import engine.ui.CheckBoxGroup;
import java.awt.Point;

public class TroplayCheckBox extends CheckBox {
    public TroplayCheckBox(CheckBoxGroup group, Point coords) {
        super(group, coords, Layer.UI, "checkbox/unchecked.png", "checkbox/checked.png");
    }
}
