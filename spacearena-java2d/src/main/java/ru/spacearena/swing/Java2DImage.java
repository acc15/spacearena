package ru.spacearena.swing;

import ru.spacearena.engine.graphics.Image;

import java.awt.image.ImageObserver;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class Java2DImage implements Image {
    private java.awt.Image image;

    public static final ImageObserver LOAD_OBSERVER = new ImageObserver() {
        public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    };

    public Java2DImage(java.awt.Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(LOAD_OBSERVER);
    }

    public int getHeight() {
        return image.getHeight(LOAD_OBSERVER);
    }

    public <T> T getNativeImage(Class<T> clazz) {
        if (java.awt.Image.class.isAssignableFrom(clazz)) {
            return (T)image;
        }
        throw new IllegalArgumentException();
    }
}
