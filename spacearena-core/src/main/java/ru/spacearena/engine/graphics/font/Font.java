package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.texture.Texture;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
* @author Vyacheslav Mayorov
* @since 2014-07-04
*/
public class Font implements Serializable {

    public static interface Definition {
        URL getFontUrl();
        URL getTextureUrl();
        Texture.Definition getTexture();
    }

    private int imageWidth,
                imageHeight;
    private int lineHeight;
    private int originalSize;
    private Map<Character, CharGlyph> info = new HashMap<Character, CharGlyph>();

    public void setFontMetrics(int imageWidth, int imageHeight, int lineHeight, int size) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.lineHeight = lineHeight;
        this.originalSize = size;
    }

    public void setCharMetrics(char ch, int x, int y, int offset, int advance, int width) {
        final CharGlyph cm = new CharGlyph();
        cm.x = x;
        cm.y = y;
        cm.width = width;
        cm.offset = offset;
        cm.advance = advance;
        info.put(ch, cm);
    }

    public int getOriginalSize() {
        return originalSize;
    }

    public boolean isSupported(char ch) {
        return info.containsKey(ch);
    }

    public Map<Character, CharGlyph> getInfo() {
        return info;
    }

    public CharGlyph getCharInfo(char ch) {
        final CharGlyph ci = info.get(ch);
        if (ci == null) {
            throw new IllegalArgumentException("Character '" + ch + "' isn't supported by current font and therefore can't be displayed");
        }
        return ci;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

}
