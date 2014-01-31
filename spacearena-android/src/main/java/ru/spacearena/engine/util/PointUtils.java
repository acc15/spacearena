package ru.spacearena.engine.util;

import android.graphics.PointF;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class PointUtils {

    public static PointF div(PointF pt, float val) {
        pt.x /= val;
        pt.y /= val;
        return pt;
    }

    public static PointF mul(PointF pt, float val) {
        pt.x *= val;
        pt.y *= val;
        return pt;
    }

    public static PointF resize(PointF pt, float newSize) {
        return mul(pt, newSize/pt.length());
    }

    public static float scalarProduct(PointF pt1, PointF pt2) {
        return pt1.x * pt2.x + pt1.y * pt2.y;
    }

    public static float cosineOfAngle(PointF pt1, PointF pt2) {
        return scalarProduct(pt1, pt2) / (pt1.length() * pt2.length());
    }

    public static PointF identity(PointF pt) {
        return div(pt, pt.length());
    }

    public static PointF copy(PointF pt) {
        return new PointF(pt.x, pt.y);
    }

    public static PointF subtract(PointF p1, PointF p2) {
        p1.x -= p2.x;
        p1.y -= p2.y;
        return p1;
    }

    public static PointF add(PointF p1, PointF p2) {
        p1.x += p2.x;
        p1.y += p2.y;
        return p1;
    }

}
