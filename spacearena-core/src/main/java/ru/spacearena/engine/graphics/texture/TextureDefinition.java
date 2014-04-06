package ru.spacearena.engine.graphics.texture;

import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class TextureDefinition implements Texture.Definition {

    private final int type, wrapS, wrapT, minFilter, magFilter;

    public TextureDefinition() {
        this(OpenGL.TEXTURE_2D);
    }

    public TextureDefinition(int type) {
        this(type, OpenGL.LINEAR, OpenGL.LINEAR);
    }

    public TextureDefinition(int type, int minFilter, int magFilter) {
        this(type, minFilter, magFilter, 0, 0);
    }

    public TextureDefinition(int type, int minFilter, int magFilter, int wrapS, int wrapT) {
        this.type = type;
        this.wrapS = wrapS;
        this.wrapT = wrapT;
        this.minFilter = minFilter;
        this.magFilter = magFilter;
    }

    public int getWrapS() {
        return wrapS;
    }

    public int getWrapT() {
        return wrapT;
    }

    public int getMinFilter() {
        return minFilter;
    }

    public int getMagFilter() {
        return magFilter;
    }

    public int getType() {
        return type;
    }
}
