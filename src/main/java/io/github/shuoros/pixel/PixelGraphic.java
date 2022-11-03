package io.github.shuoros.pixel;

import io.github.shuoros.pixel.graphics.Graphic;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public class PixelGraphic implements Graphic {

    private BufferedImage bimg;
    private Graphics2D graphics;

    public PixelGraphic(Dimension dimension) {
        bimg = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        graphics = (Graphics2D) bimg.getGraphics();
    }

    public Graphics2D get2DGraphic() {
        return this.graphics;
    }

    public BufferedImage getBufferedImage() {
        return this.bimg;
    }

}
