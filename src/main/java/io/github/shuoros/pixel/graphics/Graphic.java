package io.github.shuoros.pixel.graphics;


import java.awt.*;
import java.awt.image.BufferedImage;

public interface Graphic {

    public Graphics2D get2DGraphic();

    public BufferedImage getBufferedImage();

}
