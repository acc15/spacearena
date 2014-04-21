package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.graphics.font.CharData;
import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.gen.pack.RectPacker;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.IntMathUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class FontGenerator {

    public static Font createFont(FontGeneratorInput input) {
        final int pointSize = FloatMathUtils.round((float)input.getFontSize()*4f/3);
        return new Font(input.getFontName(), input.getFontStyle(), pointSize);
    }

    public static BufferedImage generateFontImage(Font font, FontGeneratorInput input, FontData fd) {
        final BufferedImage img = new BufferedImage(fd.getImageWidth(), fd.getImageHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = getGraphics(img, input.isHq());
        g.setFont(font);
        g.setColor(Color.WHITE);

        final FontMetrics fm = g.getFontMetrics(font);
        final char[] ch = new char[1];
        for (CharData glyph: fd.getGlyphs()) {
            ch[0] = glyph.getCharacter();

            g.drawChars(ch, 0, 1,
                    glyph.getX() - glyph.getOffsetX(),
                    glyph.getY() - glyph.getOffsetY() + fm.getAscent());
        }
        g.dispose();
        return img;
    }

    public static FontData computeFontData(Font font, FontGeneratorInput input) {

        final String alphabet = input.getAlphabet();
        if (font.canDisplayUpTo(alphabet) >= 0) {
            return null;
        }

        final AffineTransform t = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(t, false, false);
        final FontData fi = new FontData();


        final LineMetrics lm = font.getLineMetrics(alphabet, frc);
        final int lineHeight = getAsInt(lm.getHeight());

        fi.setSpaceAdvance(getAsInt(font.getStringBounds(" ", frc).getWidth()));
        fi.setTabAdvance(fi.getSpaceAdvance() * 4);

        final ArrayList<CharData> charList = new ArrayList<CharData>();
        final int pad = input.getPad();
        final int pad2 = pad << 1;
        for (int i=0; i<alphabet.length(); i++) {
            final TextLayout textLayout = new TextLayout(alphabet.substring(i, i + 1), font, frc);
            final float ascent = textLayout.getAscent();
            final Rectangle2D tr = textLayout.getBounds();
            final Rectangle2D fr = font.getStringBounds(alphabet, i, i + 1, frc);
            final CharData charData = new CharData();
            charData.setCharacter(alphabet.charAt(i));
            charData.setAdvance(getAsInt(fr.getWidth()));
            charData.setDimension(getAsInt(tr.getWidth()) + pad2, getAsInt(tr.getHeight()) + pad2);
            charData.setOffsetX((int)Math.round(tr.getX()) - pad);
            charData.setOffsetY((int)Math.round(tr.getY() + ascent) - pad);
            charList.add(charData);
        }

        Collections.sort(charList, new Comparator<CharData>() {
            public int compare(CharData o1, CharData o2) {
                final int m1 = IntMathUtils.max(o1.getWidth(), o1.getHeight());
                final int m2 = IntMathUtils.max(o2.getWidth(), o2.getHeight());
                return -IntMathUtils.compare(m1, m2);
            }
        });

        final RectPacker packer = new RectPacker();
        for (CharData cd: charList) {
            packer.pack(cd);
            fi.setCharData(cd.getCharacter(), cd);
        }

        fi.setFontName(font.getName());
        fi.setImageWidth(IntMathUtils.pow2RoundUp(packer.getPackWidth()));
        fi.setImageHeight(IntMathUtils.pow2RoundUp(packer.getPackHeight()));
        fi.setLineHeight(lineHeight);
        fi.setFontSize(input.getFontSize());
        fi.setFontStyle(font.getStyle());
        fi.setImageScale(input.getImageScale());
        return fi;
    }

    public static Graphics2D getGraphics(Graphics g, boolean hq) {
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hq) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        return g2d;
    }

    public static Graphics2D getGraphics(BufferedImage img, boolean hq) {
        return getGraphics(img.getGraphics(), hq);
    }

    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }

}
