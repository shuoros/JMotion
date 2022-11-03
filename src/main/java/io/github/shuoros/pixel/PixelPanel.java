package io.github.shuoros.pixel;

import io.github.shuoros.pixel.window.Panel;

public class PixelPanel extends Panel {

    public PixelPanel() {
        this.dimension = App.window.getDimension();
    }

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
        graphics.fillRect(0, 0, dimension.width, dimension.height);

    }
}
