package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.graphics.font.FontData;

import java.awt.image.BufferedImage;

/**
* @author Vyacheslav Mayorov
* @since 2014-10-04
*/
class FontGeneratorResult {

    private FontData fontData;
    private BufferedImage fontTexture;
    private BufferedImage distanceFieldTexture;

    FontGeneratorResult(FontData fontData, BufferedImage fontTexture, BufferedImage distanceFieldTexture) {
        this.fontData = fontData;
        this.fontTexture = fontTexture;
        this.distanceFieldTexture = distanceFieldTexture;
    }

    public FontData getFontData() {
        return fontData;
    }

    public BufferedImage getFontTexture() {
        return fontTexture;
    }

    public BufferedImage getDistanceFieldTexture() {
        return distanceFieldTexture;
    }

}
