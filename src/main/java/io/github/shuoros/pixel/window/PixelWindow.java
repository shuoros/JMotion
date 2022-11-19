package io.github.shuoros.pixel.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PixelWindow extends Window {

    private int width;

    private int height;

    private Panel panel;

    public PixelWindow(String title, String width, String height) {
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);
        setTitle(title);
    }

    @Override
    public void construct(Panel panel) {
        this.panel = panel;
        add(this.panel);
        pack();
//        setUndecorated(true);
//        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("hey");
                System.exit(0);
            }
        });
        setVisible(true);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    @Override
    public Panel getPanel() {
        return this.panel;
    }

}
