package ru.spacearena.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class ShapeUtils {

    public static void fillPoint(float[] vertex, int offset, float x, float y) {
        vertex[offset++] = x;
        vertex[offset] = y;
    }

    public static void fillRect(float[] vertex, float left, float top, float right, float bottom) {
        fillPoint(vertex, 0, left, top);
        fillPoint(vertex, 2, left, bottom);
        fillPoint(vertex, 4, right, bottom);
        fillPoint(vertex, 6, right, top);
    }

}
