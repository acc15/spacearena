package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface Matrix {
    void multiply(Matrix transform);
    void mapPoints(float[] pts);
    void mapPoints(float[] dst, int dstOffset, float[] src, int srcOffset, int pointCount);

    boolean inverse(Matrix matrix);
    boolean isIdentity();

    void set(float pivotX, float pivotY,
             float scaleX, float scaleY,
             float skewX, float skewY,
             float rotateX, float rotateY,
             float x, float y);

    void set(float pivotX, float pivotY,
             float scaleX, float scaleY,
             float skewX, float skewY,
             float rotation,
             float x, float y);
}
