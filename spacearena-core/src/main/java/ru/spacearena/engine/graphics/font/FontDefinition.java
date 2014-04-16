package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureDefinition;

import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontDefinition implements FontData.Definition {

    private final TextureDefinition td;
    private final Class<?> baseClass;
    private final String name;


    public FontDefinition(Class<?> baseClass, String fontName) {
        this.baseClass = baseClass;
        this.name = fontName;
        this.td = new TextureDefinition(OpenGL.LINEAR, OpenGL.LINEAR);
    }

    public Texture.Definition getTexture() {
        return td;
    }

    public URL getFontUrl() {
        return baseClass.getResource(name + ".fnt");
    }

    public URL getTextureUrl() {
        return baseClass.getResource(name + ".png");
    }

}
