package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DImage implements Image {

    java.awt.Image image;

    public Java2DImage(java.awt.Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }
}
