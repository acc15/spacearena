package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.parse.Arguments;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class GLFontGenerator {


    public static class Args {
        public String fontName;
        public int fontSize;
        public int fontStyle;
        public int width;
        public int pad;
        public boolean mipmap;
        public boolean hq;
    }

    public static int parseFontStyle(String n) {
        if (n.isEmpty() || "p".equals(n) || "plain".equals(n)) {
            return java.awt.Font.PLAIN;
        }
        if ("b".equals(n) || "bold".equals(n)) {
            return java.awt.Font.BOLD;
        }
        if ("i".equals(n) || "italic".equals(n)) {
            return java.awt.Font.ITALIC;
        }
        throw new IllegalArgumentException("Unknown font style: " + n);
    }

    public static Args parseArgs(String[] args) {
        final Arguments a = Arguments.parse(args);
        final Args p = new Args();
        p.fontName = a.getValue("font");
        p.fontSize = a.getInt("size", 24);
        p.fontStyle = parseFontStyle(a.getValue("style"));
        p.pad = a.getInt("pad", 2);
        p.width = a.getInt("width", 512);
        p.hq = a.has("hq");
        p.mipmap = a.has("mipmap");
        return p;
    }


    public static Font computeFontInfo(java.awt.Font font, int maxWidth, int pad, char[] alphabet) {

        final AffineTransform t = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(t, true, true);
        final Font fi = new Font();

        final LineMetrics lm = font.getLineMetrics(alphabet, 0, alphabet.length, frc);
        final int lineHeight = getAsInt(lm.getHeight());

        fi.setSpaceAdvance(getAsInt(font.getStringBounds(" ", frc).getWidth()));
        fi.setTabAdvance(fi.getSpaceAdvance() * 4);

        int x = 0, y = 0;
        for (int i=0; i<alphabet.length; i++) {
            final TextLayout textLayout = new TextLayout(Character.toString(alphabet[i]), font, frc);

            final Rectangle2D tr = textLayout.getBounds();
            final Rectangle2D fr = font.getStringBounds(alphabet, i, i + 1, frc);
            final int advance = getAsInt(fr.getWidth());
            final int offset = (int)Math.round(tr.getX()) - pad;
            final int width = getAsInt(tr.getWidth()) + pad * 2;

            if (x + width > maxWidth) {
                x = 0;
                y += lineHeight;
            }

            fi.setCharMetrics(alphabet[i], x, y, offset, advance, width);
            x += width;

        }
        fi.setFontMetrics(y == 0 ? x : maxWidth, pow2RoundUp(y+lineHeight), lineHeight, font.getSize());
        return fi;
    }

    public static int pow2RoundUp (int x) {
        if (x < 0) {
            return 0;
        }
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x+1;
    }

    private static String suggestFileName(java.awt.Font font) {
        final String name = font.getFontName();
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.length(); i++) {
            final char ch = name.charAt(i);
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                sb.append(Character.toLowerCase(ch));
            }
        }
        switch (font.getStyle()) {
        case java.awt.Font.BOLD: sb.append("_b"); break;
        case java.awt.Font.ITALIC: sb.append("_i"); break;
        }
        return sb.toString();
    }

    public static int storeWithMipMaps(BufferedImage image, Args args, int level, String format) {
        try {
            ImageIO.write(image, "png", new File(String.format(format,level)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!args.mipmap) {
            return level;
        }

        final int iw = image.getWidth(), ih = image.getHeight();
        int w = iw >> 1, h = ih >> 1;
        if (w < 1 && h < 1) {
            return level;
        }

        w = Math.max(w,1);
        h = Math.max(h,1);

        final BufferedImage mipmap = new BufferedImage(w,h,image.getType());
        final Graphics2D g = getGraphics(mipmap, args.hq);
        g.drawImage(image, 0, 0, w, h, 0, 0, iw, ih, null);
        g.dispose();
        return storeWithMipMaps(mipmap, args, level+1, format);

    }

    public static Graphics2D getGraphics(BufferedImage img, boolean hq) {
        final Graphics2D g = (Graphics2D)img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hq) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        return g;
    }


    public static void main(String[] args) {

        final Args parsedArgs = parseArgs(args);

        final String fontName = parsedArgs.fontName;
        final int style = parsedArgs.fontStyle;
        final int size = parsedArgs.fontSize;

        final char[] alphabet = "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final int imageWidth = pow2RoundUp(parsedArgs.width);

        final java.awt.Font font = new java.awt.Font(fontName, style, size);
        final Font fi = computeFontInfo(font, imageWidth, parsedArgs.pad, alphabet);

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
    }

    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }

}
