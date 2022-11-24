package io.github.shuoros.pixel.window;

import io.github.shuoros.pixel.graphics.Scene;
import io.github.shuoros.pixel.io.InputController;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPanel extends JPanel {

    private Scene scene;

    public void construct(Dimension dimension) {
        setPreferredSize(dimension);
        setFocusable(true);
        requestFocus();
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public abstract void update();

    public abstract void input(InputController controller);

    public abstract void render(Graphics2D graphics, Scene scene);

}
