package ru.spacearena.engine.util;

import ru.spacearena.engine.geometry.shapes.PolyShape2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class ShapeUtils {

    public static float[] transformShape(PolyShape2F shape, Matrix matrix) {
        final int pointCount = shape.getPointCount();
        if (pointCount > TempUtils.POINT_BUF.length/2) {
            throw new RuntimeException("POINT_BUF overflow");
        }
        shape.getPoints(TempUtils.POINT_BUF, 0, pointCount);
        matrix.transformPoints(TempUtils.POINT_BUF, 0, TempUtils.POINT_BUF, 0, pointCount);
        //matrix.tranmapPoints(TempUtils.POINT_BUF, 0, TempUtils.POINT_BUF, 0, pointCount);
        return TempUtils.POINT_BUF;
    }

    public static void computeBoundingBox(PolyShape2F shape, Rect2FPP rect, Matrix matrix) {
        final int pointCount = shape.getPointCount();
        if (pointCount > 0) {
            final float[] pts = transformShape(shape, matrix);
            rect.set(pts[0], pts[1], pts[0], pts[1]);
            rect.computeBoundingBox(pts, 2, pointCount-1);
        }
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
