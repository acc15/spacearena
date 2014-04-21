package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.Texture;
import ru.spacearena.engine.util.ResourceUtils;

import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontDefinition implements FontData.Definition {

    private final Texture.Definition td;
    private final URL fontUrl;

    public FontDefinition(Class<?> baseClass, String fontName) {
        this.fontUrl = ResourceUtils.getUrl(baseClass, fontName + ".fnt");
        this.td = new Texture.Definition().url(baseClass, fontName + ".png");
    }

    public Texture.Definition getTexture() {
        return td;
    }

    public URL getFontUrl() {
        return fontUrl;
    }

}
