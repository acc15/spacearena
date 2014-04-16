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

    public float getLeft() {
        return 0;
    }

    public float getRight() {
        return 1;
    }

    public float getTop() {
        return flipY ? 1 : 0;
    }

    public float getBottom() {
        return flipY ? 0 : 1;
    }

    public float computeX(float x) {
        return x;
    }

    public float computeY(float y) {
        return flipY ? 1-y : y;
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
