package ru.spacearena.engine.graphics.font.gen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class ImageUtils {


    public static BufferedImage loadImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Can't load image from url: " + url, e);
        }

    }

}
