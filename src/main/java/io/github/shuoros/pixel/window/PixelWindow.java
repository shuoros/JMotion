package io.github.shuoros.pixel.window;

import javax.swing.*;
import java.awt.*;

public class PixelWindow extends AbstractWindow {

    private int width;

    private int height;

    private AbstractPanel panel;

    public PixelWindow(String title, boolean undecorated) {
        this(title, undecorated, (int) SysScreen.getWidth(), (int) SysScreen.getHeight());
    }

    public PixelWindow(String title, boolean undecorated, int width, int height) {
        this.width = width;
        this.height = height;
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(undecorated);
        if (undecorated) {
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        }
    }

    @Override
    public void construct(AbstractPanel panel) {
        this.panel = panel;
        add(this.panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    @Override
    public AbstractPanel getPanel() {
        return this.panel;
    }

}
