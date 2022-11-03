package io.github.shuoros.pixel.window;

import io.github.shuoros.pixel.io.InputController;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    public void construct(Dimension dimension) {
        setPreferredSize(dimension);
        setFocusable(true);
        requestFocus();
    }

    public abstract void update();

    public abstract void input(InputController controller);

    public abstract void render(Graphics2D graphics);

}
