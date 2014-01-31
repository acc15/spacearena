package ru.spacearena.swing;

import ru.spacearena.engine.PlatformManager;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class Java2DPlatformManager implements PlatformManager {

    private String resourcePrefix;

    public Java2DPlatformManager(String packageName) {
        this.resourcePrefix = packageName.replace('.', '/');
    }

    public Image loadImage(String name) {
        final String imagePath = resourcePrefix + "/" + name + ".png";
        final URL resourceUrl = getClass().getClassLoader().getResource(imagePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Can't load " + name + " resource");
        }
        try {
            return new Java2DImage(ImageIO.read(resourceUrl));
        } catch (IOException e) {
            throw new RuntimeException("Can't load image by URL: " + resourceUrl, e);
        }
    }

    public Matrix createMatrix() {
        return new Java2DMatrix();
    }
}
