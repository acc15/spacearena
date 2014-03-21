package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface Matrix {
    void multiply(Matrix transform);
    void mapPoints(float[] pts);
    void mapPoints(float[] dst, int dstOffset, float[] src, int srcOffset, int pointCount);
    void mapPoint(Point2F point);
    void mapPoint(Point2F point, Point2F out);

    boolean inverse(Matrix matrix);
    boolean isIdentity();

    void set(float pivotX, float pivotY,
             float scaleX, float scaleY,
             float skewX, float skewY,
             float radians,
             float x, float y);

    void set(float x, float y, float radians);
}
