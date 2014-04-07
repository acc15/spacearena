package ru.spacearena.engine.graphics.texture;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-05-04
 */
public class Texture {

    public interface Definition {
        int getWrapS();
        int getWrapT();
        int getMinFilter();
        int getMagFilter();
        int getType();
    }

    private int id;
    private int width;
    private int height;
    private boolean flipY = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public float getAspectRatio() {
        return (float)width/height;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }
}
