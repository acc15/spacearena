package ru.spacearena.engine.geometry.intersections;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
* @author Vyacheslav Mayorov
* @since 2014-09-03
*/
public class LineLineIntersection {
    float x, y;
    float at, bt;
    boolean intersects;

    public float getIntersectionX() {
        return x;
    }

    public float getIntersectionY() {
        return y;
    }

    public float getAMultiplier() {
        return at;
    }

    public float getBMultiplier() {
        return bt;
    }

    public boolean isSegmentAIntersects() {
        return intersects && FloatMathUtils.inRange(at, 0, 1);
    }

    public boolean isSegmentBIntersects() {
        return intersects && FloatMathUtils.inRange(bt, 0, 1);
    }

    public boolean isIntersects() {
        return intersects;
    }
}
