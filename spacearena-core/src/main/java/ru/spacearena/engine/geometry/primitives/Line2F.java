package ru.spacearena.engine.geometry.primitives;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Line2F {

    public float x1, y1, x2, y2;

    public Line2F() {
    }

    public Line2F(float x1, float y1, float x2, float y2) {
        set(x1, y1, x2, y2);
    }

    public void set(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public float lengthSquare() {
        return FloatMathUtils.lengthSquare(x2 - x1, y2 - y1);
    }

    public float length() {
        return FloatMathUtils.sqrt(lengthSquare());
    }

}
