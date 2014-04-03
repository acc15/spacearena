package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class Color {

    public static final Color BLACK =         new Color(0,0,0,1);
    public static final Color WHITE =         new Color(1,1,1,1);
    public static final Color RED =           new Color(1,0,0,1);
    public static final Color GREEN =         new Color(0,1,0,1);
    public static final Color BLUE =          new Color(0,0,1,1);
    public static final Color YELLOW =        new Color(1,1,0,1);
    public static final Color GRAY =          new Color(0.5f,0.5f,0.5f,1);
    public static final Color DARK_GRAY =     new Color(1/3f,1/3f,1/3f,1f);
    public static final Color LIGHT_GRAY =    new Color(2/3f,2/3f,2/3f,1f);

    public float r,g,b,a;

    public Color() {
        set(BLACK);
    }

    public Color(Color color) {
        set(color);
    }

    public Color(float r, float g, float b) {
        set(r,g,b);
    }

    public Color(float r, float g, float b, float a) {
        set(r,g,b,a);
    }

    public void set(Color color) {
        set(color.r, color.g, color.b, color.a);
    }

    public void set(float r, float g, float b) {
        set(r,g,b,1);
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

}
