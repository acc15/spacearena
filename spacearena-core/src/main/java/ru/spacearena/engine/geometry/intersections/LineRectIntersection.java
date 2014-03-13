package ru.spacearena.engine.geometry.intersections;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
* @author Vyacheslav Mayorov
* @since 2014-09-03
*/
public class LineRectIntersection {

    float x0, y0, x1, y1;
    float t0, t1;
    boolean intersects;

    public float getFirstIntersectionX() {
        return x0;
    }

    public float getFirstIntersectionY() {
        return y0;
    }

    public float getSecondIntersectionX() {
        return x1;
    }

    public float getSecondIntersectionY() {
        return y1;
    }

    public float getFirstIntersectionMultiplier() {
        return t0;
    }

    public float getSecondIntersectionMultiplier() {
        return t1;
    }

    public boolean isIntersects() {
        return intersects;
    }

    public boolean isStartSegmentIntersects() {
        return FloatMathUtils.inRange(t0, 0, 1);
    }

    public boolean isEndSegmentIntersects() {
        return FloatMathUtils.inRange(t1, 0, 1);
    }

    public boolean isSegmentIntersects() {
        return isStartSegmentIntersects() && isEndSegmentIntersects();
    }
}
