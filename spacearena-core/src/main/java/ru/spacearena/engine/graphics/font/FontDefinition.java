package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureDefinition;

import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontDefinition implements Font.Definition {

    public enum Quality {
        HIGH,
        MEDIUM,
        LOW
    }


    private final TextureDefinition td;
    private final Class<?> baseClass;
    private final String name;

    public static int getMinFilter(Quality quality) {
        switch (quality) {
        case LOW: return OpenGL.NEAREST_MIPMAP_NEAREST;
        case MEDIUM: return OpenGL.NEAREST_MIPMAP_LINEAR;
        case HIGH: return OpenGL.LINEAR_MIPMAP_LINEAR;
        }
        throw new IllegalArgumentException("unknown quality");
    }

    public static int getMagFilter(Quality quality) {
        switch (quality) {
        case LOW: return OpenGL.NEAREST;
        case MEDIUM:
        case HIGH: return OpenGL.LINEAR;
        }
        throw new IllegalArgumentException("unknown quality");
    }

    public FontDefinition(Class<?> baseClass, Quality quality, String fontName) {
        this.baseClass = baseClass;
        this.name = fontName;
        this.td = new TextureDefinition(getMinFilter(quality), getMagFilter(quality));
    }

    public Texture.Definition getTexture() {
        return td;
    }

    public URL getFontUrl() {
        return baseClass.getResource(name + ".fnt");
    }

    public URL getTextureUrl(int level) {
        return baseClass.getResource(name + level + ".png");
    }

}
