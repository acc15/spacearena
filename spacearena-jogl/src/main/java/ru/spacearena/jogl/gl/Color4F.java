package ru.spacearena.jogl.gl;

import java.nio.FloatBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class Color4F {

    public float r,g,b,a;

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
