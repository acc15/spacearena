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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
