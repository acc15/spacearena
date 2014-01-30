package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Color {

    public static final Color BLACK = new Color(0xff000000);
    public static final Color WHITE = new Color(0xffffffff);

    private int value;

    public Color(int value) {
        this.value = value;
    }

    public static Color fromARGB(int alpha, int red, int green, int blue) {
        return new Color(alpha << 24 | red << 16 | green << 8 | blue);
    }

    public static Color fromRGB(int red, int green, int blue) {
        return fromARGB(0xff, red, green, blue);
    }

    public int getValue() {
        return value;
    }
}
