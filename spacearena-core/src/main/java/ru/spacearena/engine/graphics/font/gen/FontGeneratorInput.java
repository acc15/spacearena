package ru.spacearena.engine.graphics.font.gen;

import java.awt.*;

/**
* @author Vyacheslav Mayorov
* @since 2014-10-04
*/
class FontGeneratorInput {

    private Font font;
    private int pad;
    //private float distanceFieldOffset;
    //private float distanceFieldScale;
    private float distanceFieldSpread;
    private String alphabet;
    private boolean hq;
    private int imageScale;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public float getDistanceFieldSpread() {
        return distanceFieldSpread;
    }

    public void setDistanceFieldSpread(float distanceFieldSpread) {
        this.distanceFieldSpread = distanceFieldSpread;
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
}
