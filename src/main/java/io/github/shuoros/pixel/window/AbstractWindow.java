package io.github.shuoros.pixel.window;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {

    protected static final Rectangle SysScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

    public abstract void construct(AbstractPanel panel);

    public abstract Dimension getDimension();

    public abstract AbstractPanel getPanel();

}
