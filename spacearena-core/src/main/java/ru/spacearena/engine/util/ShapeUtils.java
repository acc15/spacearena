package ru.spacearena.engine.util;

import ru.spacearena.engine.geometry.shapes.PolyShape2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
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
        return TempUtils.POINT_BUF;
    }

    public static void computeBoundingBox(PolyShape2F shape, Rect2FP rect, Matrix matrix) {
        final int pointCount = shape.getPointCount();
        if (pointCount > 0) {
            final float[] pts = transformShape(shape, matrix);
            rect.setBounds(pts[0], pts[1], pts[0], pts[1]);
            // TODO
            //rect.computeBoundingBox(pts, 2, pointCount-1);
        }
    }

}
