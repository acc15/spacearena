package ru.spacearena.engine.graphics.font.df;

import ru.spacearena.engine.graphics.font.GLFontGenerator;
import ru.spacearena.engine.util.FloatMathUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class DeadReckoning {

    public static float getComponent(int c, int b) {
        final int offset = b << 3;
        final int mask = 0xff << offset;
        final int v = (c & mask) >>> offset;
        return v / 255f;
    }

    public static boolean getPixel(BufferedImage image, int x, int y) {
        final int w = image.getWidth(), h = image.getHeight();
        final int c = x < 0 || y < 0 || x >= w || y >= h ? 0 : image.getRGB(x,y);

        final float a = getComponent(c,3), r = getComponent(c,2), g = getComponent(c,1), b = getComponent(c,0);
        final float v = (r+g+b)/3f * a;
        return v > 0.5f;
    }

    public static void compare(DistanceField g, int x, int y, int xNeighbor, int yNeighbor, float d) {
        if (g.getDistance(xNeighbor, yNeighbor) + d < g.getDistance(x, y)) {
            final int borderX = g.getBorderX(xNeighbor, yNeighbor), borderY = g.getBorderY(xNeighbor, yNeighbor);
            g.set(x, y, borderX, borderY, FloatMathUtils.length(x - borderX, y - borderY));
        }
    }

//    public static void setPoint(Grid g, int x, int y, float d, int x1, int y1) {
//        final Point p = g.get(x1,y1);
//        if (g.getDistance(x,y) + d < p.d) {
//            p.set(g.getX(x,y), g.getY(x,y));
//            p.d = FloatMathUtils.length(x1-p.x,y1-p.y);
//    }

    public static DistanceField computeDistanceField(BufferedImage image, float d1, float d2) {

        final int w = image.getWidth(), h = image.getHeight();

        final DistanceField g = new DistanceField(w, h);

        // initializing df
        for (int y=0; y<h; y++) {
            for (int x=0;x<w;x++) {
                final boolean c = getPixel(image, x, y);
                if (c != getPixel(image, x-1, y) ||
                    c != getPixel(image, x, y-1) ||
                    c != getPixel(image, x+1, y) ||
                    c != getPixel(image, x, y+1)) {
                    g.set(x,y, x,y, 0);
                } else {
                    g.set(x,y, DistanceField.DEFAULT_BORDER, DistanceField.DEFAULT_BORDER, DistanceField.DEFAULT_DISTANCE);
                }
            }
        }

        for (int y=0; y<h; y++) {
            for (int x=0;x<w;x++) {
                compare(g, x, y, x - 1, y - 1, d2);
                compare(g, x, y, x, y - 1, d1);
                compare(g, x, y, x + 1, y - 1, d2);
                compare(g, x, y, x - 1, y, d1);
            }
        }

        for (int y=h-1; y>=0; y--) {
            for (int x=w-1;x>=0;x--) {
                compare(g, x, y, x + 1, y, d1);
                compare(g, x, y, x - 1, y + 1, d2);
                compare(g, x, y, x, y + 1, d1);
                compare(g, x, y, x + 1, y + 1, d2);
            }
        }

        for (int y=0; y<h; y++) {
            for (int x=0;x<w;x++) {
                final boolean c = getPixel(image, x, y);
                if (!c) {
                    g.negateDistance(x,y);
                }
            }
        }
        return g;
    }

    public static int toColor(float c, int offset) {
        return (int)(FloatMathUtils.min(FloatMathUtils.max(0,c), 1) * 255) << offset;
    }

    public static int toColor(float a, float r, float g, float b) {
        return toColor(a,24) | toColor(r,16) | toColor(g,8) | toColor(b,0);
    }

    public static interface DistanceMap {
        float mapDistance(float d);
    }

    public static class ManualMap implements DistanceMap {
        private float offset;
        private float invScale;

        public ManualMap(float offset, float scale) {
            this.offset = offset;
            this.invScale = 1/scale;
        }

        public float mapDistance(float d) {
            return (d + offset) * invScale;
        }
    }


    public static class MixMap implements DistanceMap {

        private float min = 0, max = 0;

        public MixMap(DistanceField g) {
            for (int y=0; y<g.getHeight(); y++) {
                for (int x=0;x<g.getWidth(); x++) {
                    final float d = g.getDistance(x,y);
                    if (d < min) {
                        min = d;
                    }
                    if (d > max) {
                        max = d;
                    }
                }
            }
        }

        public float mapDistance(float d) {
            return (d - min) / (max - min);
        }
    }

    /*
    public static class MinMaxMap implements DistanceMap {
        private float minMaxDistance;

        public MinMaxMap(float minMaxDistance) {
            this.minMaxDistance = minMaxDistance;
        }

        public float mapDistance(DistanceField grid, float d) {
            float min = grid.getMinDistance(), max = grid.getMaxDistance();
            if (max < minMaxDistance) {
                final float v = minMaxDistance - max;
                d += v;
                max += v;
                min += v;
            }

            if (d < 0) {
                return 0.5f - (d / min) * 2f;
            } else {
                return 0.5f + (d / max) * 0.5f;
            }
        }
    }
    */
    public static class SpreadMap implements DistanceMap {
        private float spread;

        public SpreadMap(DistanceField g, int scale) {
            this.spread = (float)Math.min(g.getWidth(), g.getHeight()) / (1 << scale);
        }

        public float mapDistance(float d) {
            return d < 0
                    ?        0.5f * (d + spread) / spread
                    : 0.5f + 0.5f * d / spread;
        }
    }

    public static BufferedImage toImage(DistanceField g, DistanceMap map) {
        final BufferedImage b = new BufferedImage(g.getWidth(), g.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y=0; y<g.getHeight(); y++) {
            for (int x=0;x<g.getWidth(); x++) {
                final float d = g.getDistance(x,y);
                final float c = map.mapDistance(d);
                b.setRGB(x,y,toColor(1, c,c,c));
            }
        }
        return b;
    }

    public static BufferedImage resizeTo(BufferedImage image, float scale) {
        final int oldWidth = image.getWidth(), oldHeight = image.getHeight();
        final int newWidth = (int)(oldWidth * scale), newHeight = (int)(oldHeight * scale);
        final BufferedImage sized = new BufferedImage(newWidth, newHeight, image.getType());
        final Graphics2D g2d = GLFontGenerator.getGraphics(sized, true);
        g2d.drawImage(image,0,0,newWidth,newHeight,0,0,oldWidth,oldHeight,null);
        g2d.dispose();
        return sized;
    }

    public static void main(String[] args) throws Exception {
        final File file = new File("df-original-segoe.png");
        final BufferedImage i = ImageIO.read(file);

        final float d = 1;
        final DistanceField g = computeDistanceField(i, d, FloatMathUtils.length(d,d));
        final BufferedImage df = toImage(g, new ManualMap(16, 20));
        ImageIO.write(df, "png", new File("df-transform-segoe.png"));

        final BufferedImage sf = resizeTo(df, 1/4f);
        ImageIO.write(sf, "png", new File("df-size-segoe.png"));
    }

}
