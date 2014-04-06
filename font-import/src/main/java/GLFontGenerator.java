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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class GLFontGenerator {


    public static final class CharInfo {
        int x, y, offset, advance, width;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getOffset() {
            return offset;
        }

        public int getWidth() {
            return width;
        }

        public int getAdvance() {
            return advance;
        }
    }

    public static class FontInfo {

        private int imageWidth,
                    imageHeight;
        private int lineHeight;
        private int originalSize;
        private Map<Character, CharInfo> info = new HashMap<Character, CharInfo>();

        public void setFontMetrics(int imageWidth, int imageHeight, int lineHeight, int size) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.lineHeight = lineHeight;
            this.originalSize = size;
        }

        public void setCharMetrics(char ch, int x, int y, int offset, int advance, int width) {
            final CharInfo cm = new CharInfo();
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

        public Map<Character, CharInfo> getInfo() {
            return info;
        }

        public CharInfo getCharInfo(char ch) {
            return info.get(ch);
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

    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }


    public static FontInfo computeFontInfo(Font font, int maxWidth, int pad, char[] alphabet) {

        final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        final FontInfo fi = new FontInfo();

        final LineMetrics lm = font.getLineMetrics(alphabet, 0, alphabet.length, frc);
        final int lineHeight = getAsInt(lm.getHeight());

        int x = 0, y = 0;
        for (int i=0; i<alphabet.length; i++) {
            final TextLayout textLayout = new TextLayout(Character.toString(alphabet[i]), font, frc);

            final Rectangle2D tr = textLayout.getBounds();
            final Rectangle2D fr = font.getStringBounds(alphabet, i, i + 1, frc);
            final int advance = getAsInt(fr.getWidth());
            final int offset = (int)Math.round(tr.getX());
            final int width = getAsInt(tr.getWidth());

            final int padOffset = offset + pad;
            final int padWidth = width + pad * 2;
            if (x + padWidth > maxWidth) {
                x = 0;
                y += lineHeight;
            }

            fi.setCharMetrics(alphabet[i], x, y, offset, advance, width);
            x += width;

        }
        fi.setFontMetrics(y == 0 ? x : maxWidth, y+lineHeight, lineHeight, font.getSize());
        return fi;
    }

    public static void main(String[] args) {

        final String fontName = "Shonar Bangla";
        final int style = Font.PLAIN;
        final int size = 72;

        final char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final int imageWidth = 500;

        final Font font = new Font(fontName, style, size);

        final FontInfo fi = computeFontInfo(font, imageWidth, 10, alphabet);

        final BufferedImage img = new BufferedImage(fi.getImageWidth(), fi.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) img.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(Color.WHITE);

        final int ascent = g.getFontMetrics().getAscent();
        for (int i = 0; i < alphabet.length; i++) {
            final char ch = alphabet[i];
            final CharInfo ci = fi.getCharInfo(ch);
            g.drawChars(alphabet, i, 1, ci.getX() - ci.getOffset(), ci.getY() + ascent);
        }
//
        try {
            ImageIO.write(img, "png", new File(font.getName() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
