package io.github.shuoros.pixel.graphics;

import java.awt.*;

public interface Scene {

    int x();
    int y();

    void render(Graphics2D graphics);

}
