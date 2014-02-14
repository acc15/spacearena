package ru.spacearena.engine;

import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidMatrix implements Matrix {
    final android.graphics.Matrix matrix;

    public AndroidMatrix() {
        this.matrix = new android.graphics.Matrix();
    }

    public AndroidMatrix(android.graphics.Matrix matrix) {
        this.matrix = matrix;
    }

    public void identity() {
        matrix.reset();
    }

    public void multiply(Matrix transform) {
        matrix.preConcat(((AndroidMatrix)transform).matrix);
    }

    public void translate(float x, float y) {
        matrix.preTranslate(x, y);
    }

    public void rotate(float degrees) {
        matrix.preRotate(degrees);
    }

    public void scale(float x, float y) {
        matrix.preScale(x, y);
    }

    public void skew(float x, float y) {
        matrix.preSkew(x, y);
    }

    public void mapPoints(float[] pts) {
        matrix.mapPoints(pts);
    }
}
