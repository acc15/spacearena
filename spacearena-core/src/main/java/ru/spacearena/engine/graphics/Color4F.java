package ru.spacearena.engine.graphics;

import java.nio.FloatBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class Color4F {

    public static final Color4F BLACK =         new Color4F(0,0,0,1);
    public static final Color4F WHITE =         new Color4F(1,1,1,1);
    public static final Color4F RED =           new Color4F(1,0,0,1);
    public static final Color4F GREEN =         new Color4F(0,1,0,1);
    public static final Color4F BLUE =          new Color4F(0,0,1,1);
    public static final Color4F YELLOW =        new Color4F(1,1,0,1);
    public static final Color4F GRAY =          new Color4F(0.5f,0.5f,0.5f,1);
    public static final Color4F DARK_GRAY =     new Color4F(1/3f,1/3f,1/3f,1f);
    public static final Color4F LIGHT_GRAY =    new Color4F(2/3f,2/3f,2/3f,1f);

    public float r,g,b,a;

    public Color4F() {
        set(BLACK);
    }

    public Color4F(Color4F color) {
        set(color);
    }

    public Color4F(float r, float g, float b) {
        set(r,g,b);
    }

    public Color4F(float r, float g, float b, float a) {
        set(r,g,b,a);
    }

    public void set(Color4F color) {
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

    public void put(FloatBuffer buf) {
        buf.put(r).put(g).put(b).put(a);
    }

}
