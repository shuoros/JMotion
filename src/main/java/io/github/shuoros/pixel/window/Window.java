package io.github.shuoros.pixel.window;

import javax.swing.*;
import java.awt.*;

public abstract class Window extends JFrame {

    protected static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public abstract void construct(Panel panel);

    public abstract Dimension getDimension();

    public abstract Panel getPanel();

}
