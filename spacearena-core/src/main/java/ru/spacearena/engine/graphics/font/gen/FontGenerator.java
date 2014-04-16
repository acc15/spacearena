package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.geometry.shapes.Rect2I;
import ru.spacearena.engine.graphics.font.CharData;
import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.gen.pack.RectPacker;
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


//
//    public static class Args {
//        public String fontName;
//        public int fontSize;
//        public int fontStyle;
//        public int width;
//        public int pad;
//        public boolean mipmap;
//        public boolean hq;
//    }
//
//    public static int parseFontStyle(String n) {
//        if (n.isEmpty() || "p".equals(n) || "plain".equals(n)) {
//            return java.awt.Font.PLAIN;
//        }
//        if ("b".equals(n) || "bold".equals(n)) {
//            return java.awt.Font.BOLD;
//        }
//        if ("i".equals(n) || "italic".equals(n)) {
//            return java.awt.Font.ITALIC;
//        }
//        throw new IllegalArgumentException("Unknown font style: " + n);
//    }
//
//    public static Args parseArgs(String[] args) {
//        final Arguments a = Arguments.parse(args);
//        final Args p = new Args();
//        p.fontName = a.getValue("font");
//        p.fontSize = a.getInt("size", 24);
//        p.fontStyle = parseFontStyle(a.getValue("style"));
//        p.pad = a.getInt("pad", 2);
//        p.width = a.getInt("width", 512);
//        p.hq = a.has("hq");
//        p.mipmap = a.has("mipmap");
//        return p;
//    }

    public static BufferedImage generateFontImage(Font font, FontData fd, boolean hq) {
        final BufferedImage img = new BufferedImage(fd.getImageWidth(), fd.getImageHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = getGraphics(img, hq);
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

    public static FontData computeFontData(Font font, int pad, int scale, String alphabet) {

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
        for (int i=0; i<alphabet.length(); i++) {
            final TextLayout textLayout = new TextLayout(alphabet.substring(i, i + 1), font, frc);
            final float ascent = textLayout.getAscent();
            final Rectangle2D tr = textLayout.getBounds();
            final Rectangle2D fr = font.getStringBounds(alphabet, i, i + 1, frc);
            final CharData charData = new CharData();
            charData.setCharacter(alphabet.charAt(i));
            charData.setAdvance(getAsInt(fr.getWidth()));
            charData.setWidth(getAsInt(tr.getWidth()) + pad * 2);
            charData.setHeight(getAsInt(tr.getHeight()) + pad * 2);
            charData.setOffsetX((int)Math.round(tr.getX()) - pad);
            charData.setOffsetY((int)Math.round(tr.getY() + ascent) - pad);
            charList.add(charData);
        }

        Collections.sort(charList, new Comparator<CharData>() {
            public int compare(CharData o1, CharData o2) {
                final Rect2I r1 = o1.getRect(), r2 = o2.getRect();
                final int m1 = IntMathUtils.max(r1.getWidth(), r1.getHeight());
                final int m2 = IntMathUtils.max(r2.getWidth(), r2.getHeight());
                return -IntMathUtils.compare(m1, m2);
            }
        });

        final RectPacker packer = new RectPacker();
        for (CharData cd: charList) {
            packer.pack(cd.getRect());
            fi.setCharData(cd.getCharacter(), cd);
        }

        fi.setFontName(font.getName());
        fi.setImageWidth(IntMathUtils.pow2RoundUp(packer.getPackWidth()));
        fi.setImageHeight(IntMathUtils.pow2RoundUp(packer.getPackHeight()));
        fi.setLineHeight(lineHeight);
        fi.setFontSize(font.getSize());
        fi.setFontStyle(font.getStyle());
        fi.setImageScale(scale);
        return fi;
    }




//    public static int storeWithMipMaps(BufferedImage image, Args args, int level, String format) {
//        try {
//            ImageIO.write(image, "png", new File(String.format(format,level)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (!args.mipmap) {
//            return level;
//        }
//
//        final int iw = image.getWidth(), ih = image.getH();
//        int w = iw >> 1, h = ih >> 1;
//        if (w < 1 && h < 1) {
//            return level;
//        }
//
//        w = Math.max(w,1);
//        h = Math.max(h,1);
//
//        final BufferedImage mipmap = new BufferedImage(w,h,image.getType());
//        final Graphics2D g = getGraphics(mipmap, args.hq);
//        g.drawImage(image, 0, 0, w, h, 0, 0, iw, ih, null);
//        g.dispose();
//        return storeWithMipMaps(mipmap, args, level+1, format);
//
//    }

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

    /*
    final Args parsedArgs = parseArgs(args);

    final String fontName = parsedArgs.fontName;
    final int style = parsedArgs.fontStyle;
    final int size = parsedArgs.fontSize;

    final char[] alphabet = "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    final int imageWidth = pow2RoundUp(parsedArgs.width);

    final java.awt.Font font = new java.awt.Font(fontName, style, size);
    final ru.spacearena.engine.graphics.font.Font fi = computeFontData(font, imageWidth, parsedArgs.pad, alphabet);

    final BufferedImage img = new BufferedImage(fi.getImageWidth(), fi.getImageHeight(), BufferedImage.TYPE_BYTE_GRAY);
    final Graphics2D g = getGraphics(img, parsedArgs.hq);
    g.setFont(font);
    g.setColor(Color.WHITE);

    final int ascent = g.getFontMetrics().getAscent();
    for (int i = 0; i < alphabet.length; i++) {
        final char ch = alphabet[i];
        final CharGlyph ci = fi.getCharInfo(ch);
        g.drawChars(alphabet, i, 1, ci.getX() - ci.getOffset(), ci.getY() + ascent);
    }
    g.dispose();

    final String filePrefix = suggestFileName(font);
    final int maxMipMap = storeWithMipMaps(img, parsedArgs, 0, filePrefix + "_%d.png");
    fi.setMaxMipMap(maxMipMap);
    FontIO.store(fi, new File(filePrefix + ".fnt"));
    */
    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }


}
