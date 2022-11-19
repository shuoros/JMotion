package io.github.shuoros.pixel.window;

import io.github.shuoros.pixel.io.InputController;

import java.awt.*;

public class PixelPanel extends Panel {

    @Override
    public void input(InputController controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

}
