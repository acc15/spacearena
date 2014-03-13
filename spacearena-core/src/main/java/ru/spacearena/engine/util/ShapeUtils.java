package ru.spacearena.engine.util;

import ru.spacearena.engine.graphics.Matrix;
import ru.vmsoftware.math.geometry.shapes.PolyShape2F;
import ru.vmsoftware.math.geometry.shapes.Rect2FPP;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class ShapeUtils {

    public static final float[] POINT_BUF = new float[200];

    public static float[] transformShape(PolyShape2F shape, Matrix matrix) {
        final int pointCount = shape.getVertexCount();
        if (pointCount > POINT_BUF.length/2) {
            throw new RuntimeException("POINT_BUF overflow");
        }
        shape.getPoints(POINT_BUF, 0, pointCount);
        matrix.mapPoints(POINT_BUF, 0, POINT_BUF, 0, pointCount);
        return POINT_BUF;
    }

    public static void computeBoundingBox(PolyShape2F shape, Rect2FPP rect, Matrix matrix) {
        final int pointCount = shape.getVertexCount();
        rect.computeBoundingBox(transformShape(shape, matrix), 0, pointCount);
    }

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
