package io.github.shuoros.pixel;

import io.github.shuoros.pixel.window.Panel;
import io.github.shuoros.pixel.window.Window;

import javax.swing.*;
import java.awt.*;

public class PixelWindow extends Window {

    private String title;
    private Dimension dimension;
    private Panel panel;

    public PixelWindow(String title, Dimension dimension) {
        this.title = title;
        this.dimension = dimension;
    }

    @Override
    public void construct(Panel panel) {
        this.panel = panel;
        setTitle(this.title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Dimension getDimension() {
        return this.dimension;
    }

    @Override
    public Panel getPanel() {
        return this.panel;
    }

}
