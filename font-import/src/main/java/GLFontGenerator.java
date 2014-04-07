import ru.spacearena.engine.graphics.font.CharGlyph;
import ru.spacearena.engine.graphics.font.Font;
import ru.spacearena.engine.graphics.font.FontIO;

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

    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }

    public static Font computeFontInfo(java.awt.Font font, int maxWidth, int pad, char[] alphabet) {

        final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        final Font fi = new Font();

        final LineMetrics lm = font.getLineMetrics(alphabet, 0, alphabet.length, frc);
        final int lineHeight = getAsInt(lm.getHeight());

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

    public static void main(String[] args) {

        final String fontName = "Segoe UI, Light";
        final int style = java.awt.Font.PLAIN;
        final int size = 24;

        final char[] alphabet = "!@#$%^&*()_+0123456789-=/|\\?.,[]`~ abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final int imageWidth = pow2RoundUp(512);

        final java.awt.Font font = new java.awt.Font(fontName, style, size);

        final Font fi = computeFontInfo(font, imageWidth, 2, alphabet);

        final BufferedImage img = new BufferedImage(fi.getImageWidth(), fi.getImageHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = (Graphics2D) img.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(Color.WHITE);

        final int ascent = g.getFontMetrics().getAscent();
        for (int i = 0; i < alphabet.length; i++) {
            final char ch = alphabet[i];
            final CharGlyph ci = fi.getCharInfo(ch);
            g.drawChars(alphabet, i, 1, ci.getX() - ci.getOffset(), ci.getY() + ascent);
        }

        FontIO.store(fi, new File(font.getName() + ".fnt"));
        try {
            ImageIO.write(img, "png", new File(font.getName() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
