package io.github.shuoros.pixel.window;

import javax.swing.*;
import java.awt.*;

public abstract class Window extends JFrame {

    public abstract void construct(Panel panel);

    public abstract String getTitle();

    public abstract Dimension getDimension();

    public abstract Panel getPanel();

}
