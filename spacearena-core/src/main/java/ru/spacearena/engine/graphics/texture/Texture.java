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

}
