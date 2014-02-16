package ru.spacearena.android.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface Matrix {
    void identity();
    void multiply(Matrix transform);
    void translate(float x, float y);
    void rotate(float degrees);
    void scale(float x, float y);
    void skew(float x, float y);
    void mapPoints(float[] pts);
    boolean inverse(Matrix matrix);

    boolean isIdentity();
}
