package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureDefinition;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontDefinition implements Font.Definition {

    private final TextureDefinition td = new TextureDefinition(OpenGL.TEXTURE_2D, OpenGL.NEAREST, OpenGL.NEAREST);
    private final URL fontUrl;
    private final URL textureUrl;

    public FontDefinition(Class<?> baseClass, String fontName) {
        this(baseClass.getResource("."), fontName);
    }

    public FontDefinition(URL context, String name) {
        try {
            this.fontUrl = new URL(context, name + ".fnt");
            this.textureUrl = new URL(context, name + ".png");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Can't compose font urls", e);
        }
    }

    public Texture.Definition getTexture() {
        return td;
    }

    public URL getFontUrl() {
        return fontUrl;
    }

    public URL getTextureUrl() {
        return textureUrl;
    }

}
