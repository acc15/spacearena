package ru.spacearena.engine.graphics.font.gen;

/**
* @author Vyacheslav Mayorov
* @since 2014-10-04
*/
class FontGeneratorInput {

    private int pad;
    private float distanceFieldOffset;
    private float distanceFieldScale;
    private String alphabet;
    private boolean hq;
    private int imageScale;
    private String fontName;
    private int fontStyle;
    private int fontSize;
    
    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public float getDistanceFieldOffset() {
        return distanceFieldOffset;
    }

    public void setDistanceFieldOffset(float distanceFieldOffset) {
        this.distanceFieldOffset = distanceFieldOffset;
    }

    public float getDistanceFieldScale() {
        return distanceFieldScale;
    }

    public void setDistanceFieldScale(float distanceFieldScale) {
        this.distanceFieldScale = distanceFieldScale;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public boolean isHq() {
        return hq;
    }

    public void setHq(boolean hq) {
        this.hq = hq;
    }

    public int getImageScale() {
        return imageScale;
    }

    public void setImageScale(int imageScale) {
        this.imageScale = imageScale;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }
}
