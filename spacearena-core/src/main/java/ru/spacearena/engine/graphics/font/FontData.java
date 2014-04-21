package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.Texture;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
* @author Vyacheslav Mayorov
* @since 2014-07-04
*/
public class FontData implements Serializable {


    public static interface Definition {
        URL getFontUrl();
        Texture.Definition getTexture();
    }

    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;

    private int fontStyle;
    private int imageScale;
    private int imageWidth, imageHeight;
    private int lineHeight;
    private int fontSize;
    private int spaceAdvance;
    private int tabAdvance;
    private String fontName;
    private Map<Character, CharData> info = new HashMap<Character, CharData>();

    public void setCharData(char ch, CharData data) {
        info.put(ch, data);
    }

    public CharData getCharInfo(char ch) {
        final CharData ci = info.get(ch);
        if (ci == null) {
            throw new IllegalArgumentException("Character '" + ch +
                    "' isn't supported by current font and therefore can't be displayed");
        }
        return ci;
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
        default: return getCharInfo(ch).getOffsetX();
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

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getFontSize() {
        return fontSize;
    }

    public boolean isSupported(char ch) {
        return info.containsKey(ch);
    }

    public Collection<CharData> getGlyphs() {
        return info.values();
    }
//
//    public Rect2I computeBoundingBox(String str) {
//        return computeBoundingBox(str, new Rect2IP());
//    }
//
//    public Rect2I computeBoundingBox(String str, Rect2I rect) {
//        //rect.setBounds();
//    }

    public static int getLineCount(String str) {
        if (str.isEmpty()) {
            return 0;
        }

        int lc = 1;

        boolean skipLF = false;
        for (int i=0;i<str.length(); i++) {
            final char ch = str.charAt(i);
            if (skipLF && ch != '\r') {
                skipLF = false;
                continue;
            }
            switch (ch) {
                case '\r':
                    skipLF = true;

                case '\n':
                    ++lc;
                    break;
            }
        }
        return lc;
    }

    public int getStringHeight(String str) {
        return getLineCount(str) * lineHeight;
    }

    public float getStringWidth(String str) {
        int w = 0;
        for (int i=0; i<str.length(); i++) {
            final char ch = str.charAt(i);
            w += getCharAdvance(ch);
        }
        return w;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }


    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getImageScale() {
        return imageScale;
    }

    public void setImageScale(int imageScale) {
        this.imageScale = imageScale;
    }
}
