package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Color {

    public static final int BLACK = 0xff000000;
    public static final int WHITE = 0xffffffff;
    public static final int RED =   0xffff0000;
    public static final int GREEN = 0xff00ff00;
    public static final int BLUE =  0xff0000ff;

    public static int toByteComponent(float f) {
        return (int)(f * 0xff);
    }

    public static int argb(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
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


}
