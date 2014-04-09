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
public class FontData implements Serializable {


    public void setMaxMipMap(int maxMipMap) {
        this.maxMipMap = maxMipMap;
    }

    public static interface Definition {
        URL getFontUrl();
        URL getTextureUrl(int level);
        Texture.Definition getTexture();
    }

    private int imageWidth,
                imageHeight;
    private int maxMipMap = 0;
    private int lineHeight;
    private int originalSize;
    private int spaceAdvance;
    private int tabAdvance;
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

    public int getSpaceAdvance() {
        return spaceAdvance;
    }

    public void setSpaceAdvance(int spaceAdvance) {
        this.spaceAdvance = spaceAdvance;
    }

    public int getTabAdvance() {
        return tabAdvance;
    }

    public void setTabAdvance(int tabAdvance) {
        this.tabAdvance = tabAdvance;
    }

    public int getCharOffset(char ch) {
        switch (ch) {
        case ' ':case '\t':case '\n':case '\r': return 0;
        default: return getCharInfo(ch).getOffset();
        }
    }

    public int getCharAdvance(char ch) {
        switch (ch) {
        case ' ': return spaceAdvance;
        case '\t': return tabAdvance;
        case '\n':case '\r': return 0;
        default: return getCharInfo(ch).getAdvance();
        }
    }

    public int getOriginalSize() {
        return originalSize;
    }

    public boolean isSupported(char ch) {
        return info.containsKey(ch);
    }

    public Map<Character, CharGlyph> getGlyphs() {
        return info;
    }

    public CharGlyph getCharInfo(char ch) {
        final CharGlyph ci = info.get(ch);
        if (ci == null) {
            throw new IllegalArgumentException("Character '" + ch + "' isn't supported by current font and therefore can't be displayed");
        }
        return ci;
    }

    public float getStringWidth(String str) {
        int w = 0;
        for (int i=0; i<str.length(); i++) {
            final char ch = str.charAt(i);
            w += getCharAdvance(ch);
        }
        return w;
    }

    public int getMaxMipMap() { return maxMipMap; }

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
