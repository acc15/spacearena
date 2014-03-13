package ru.spacearena.engine.geometry.intersections;

import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
public class IntersectionUtils {

    public static float intersectLineLineSimple(float ax1, float ay1, float ax2, float ay2,
                                                float bx1, float by1, float bx2, float by2) {
        final float bx = bx2 - bx1, by = by2 - by1;
        return FloatMathUtils.cross(bx1 - ax1, by1 - ay1, bx, by) /
               FloatMathUtils.cross(ax2 - ax1, ay2 - ay1, bx, by);
    }

    public static boolean isIntersects(float f) {
        return !Float.isInfinite(f);
    }

    public static float computeIntersectionX(float ax1, float ay1, float ax2, float ay2, float r) {
        return ax1 + (ax2 - ax1) * r;
    }

    public static float computeIntersectionY(float ax1, float ay1, float ax2, float ay2, float r) {
        return ay1 + (ay2 - ay1) * r;
    }

    public static void intersectLineLine(float ax1, float ay1, float ax2, float ay2,
                                         float bx1, float by1, float bx2, float by2,
                                         LineLineIntersection intersection) {

        final float ax = ax2 - ax1, ay = ay2 - ay1, bx = bx2 - bx1, by = by2 - by1;
        final float d = FloatMathUtils.cross(ax, ay, bx, by);
        if (FloatMathUtils.isZero(d)) {
            intersection.intersects = false;
            return;
        }

        final float tx = bx1 - ax1, ty = by1 - ay1;
        final float t1 = FloatMathUtils.cross(tx, ty, bx, by) / d;
        final float t2 = FloatMathUtils.cross(tx, ty, ax, ay) / d;

        intersection.intersects = true;
        intersection.at = t1;
        intersection.bt = t2;
        intersection.x = ax1 + t1 * ax;
        intersection.y = ay1 + t1 * ay;

    }

    public static void intersectLineRect(float lx1, float ly1, float lx2, float ly2,
                                         float rx1, float ry1, float rx2, float ry2,
                                         LineRectIntersection lr) {
        final LineLineIntersection ll = new LineLineIntersection();
        intersectLineRect(lx1, ly1, lx2, ly2, rx1, ry1, rx2, ry2, lr, ll);
    }

    public static void intersectLineRect(float lx1, float ly1, float lx2, float ly2,
                                         float rx1, float ry1, float rx2, float ry2,
                                         LineRectIntersection lr,
                                         LineLineIntersection ll /* to avoid allocation */) {

        int c = 0;
        for (int i=0; i<4; i++) {

            float ex1, ey1, ex2, ey2;
            switch (i) {
            case 0: ex1 = rx2; ey1 = ry1; ex2 = rx1; ey2 = ry1; break; // top edge
            case 1: ex1 = rx1; ey1 = ry1; ex2 = rx1; ey2 = ry2; break; // left edge
            case 2: ex1 = rx1; ey1 = ry2; ex2 = rx2; ey2 = ry2; break; // bottom edge
            case 3: ex1 = rx2; ey1 = ry2; ex2 = rx2; ey2 = ry1; break; // right edge
            default: throw new RuntimeException("unreachable code");
            }

            intersectLineLine(lx1, ly1, lx2, ly2, ex1, ey1, ex2, ey2, ll);
            if (!ll.isSegmentBIntersects()) {
                continue;
            }
            if (c == 0) {
                lr.t0 = ll.at;
                lr.x0 = ll.x;
                lr.y0 = ll.y;
                ++c;
                continue;
            }
            if (ll.at >= lr.t0) {
                lr.t1 = ll.at;
                lr.x1 = ll.x;
                lr.y1 = ll.y;
            } else {
                lr.t1 = lr.t0;
                lr.x1 = lr.x0;
                lr.y1 = lr.y0;
                lr.t0 = ll.at;
                lr.x0 = ll.x;
                lr.y0 = ll.y;
            }
            lr.intersects = true;
            return;
        }

        lr.intersects = false;
    }

}
