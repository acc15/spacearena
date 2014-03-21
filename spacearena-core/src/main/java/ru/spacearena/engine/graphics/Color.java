package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Color {

    public static final int BLACK =         0xff000000;
    public static final int WHITE =         0xffffffff;
    public static final int RED =           0xffff0000;
    public static final int GREEN =         0xff00ff00;
    public static final int BLUE =          0xff0000ff;
    public static final int YELLOW =        0xffffff00;
    public static final int GRAY =          0xff808080;
    public static final int LIGHT_GRAY =    0xffc0c0c0;

    public static final int ALPHA_MASK =    0xff000000;
    public static final int RED_MASK =      0x00ff0000;
    public static final int GREEN_MASK =    0x0000ff00;
    public static final int BLUE_MASK =     0x000000ff;

    public static final int ALPHA_SHIFT = 24;
    public static final int RED_SHIFT = 16;
    public static final int GREEN_SHIFT = 8;
    public static final int BLUE_SHIFT = 0;

    public static int toByteComponent(float f) {
        return (int)(f * 0xff);
    }

    public static int argb(int a, int r, int g, int b) {
        return a << ALPHA_SHIFT | r << RED_SHIFT | g << GREEN_SHIFT | b;
    }

    public static int argb(float a, float r, float g, float b) {
        return argb(toByteComponent(a), toByteComponent(r), toByteComponent(g), toByteComponent(b));
    }

    public static int rgb(int r, int g, int b) {
        return argb(0xff, r, g, b);
    }

    public static int rgb(float r, float g, float b) {
        return argb(1f, r, g, b);
    }

    public static int alpha(int color) { return (color & ALPHA_MASK) >> ALPHA_SHIFT; }
    public static int red(int color) { return (color & RED_MASK) >> RED_SHIFT; }
    public static int green(int color) { return (color & GREEN_MASK) >> GREEN_SHIFT; }
    public static int blue(int color) { return color & BLUE_MASK; }

    public static boolean hasAlpha(int color) { return (color & ALPHA_MASK) != ALPHA_MASK; }

}
