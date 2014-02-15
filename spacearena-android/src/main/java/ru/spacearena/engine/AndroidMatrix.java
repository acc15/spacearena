package ru.spacearena.engine;

import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidMatrix implements Matrix {
    final android.graphics.Matrix androidMatrix;

    public AndroidMatrix() {
        this.androidMatrix = new android.graphics.Matrix();
    }

    public AndroidMatrix(android.graphics.Matrix androidMatrix) {
        this.androidMatrix = androidMatrix;
    }

    public void identity() {
        androidMatrix.reset();
    }

    public void multiply(Matrix transform) {
        androidMatrix.preConcat(((AndroidMatrix) transform).androidMatrix);
    }

    public void translate(float x, float y) {
        androidMatrix.preTranslate(x, y);
    }

    public void rotate(float degrees) {
        androidMatrix.preRotate(degrees);
    }

    public void scale(float x, float y) {
        androidMatrix.preScale(x, y);
    }

    public void skew(float x, float y) {
        androidMatrix.preSkew(x, y);
    }

    public void mapPoints(float[] pts) {
        androidMatrix.mapPoints(pts);
    }

    public boolean inverse(Matrix matrix) {
        return androidMatrix.invert(((AndroidMatrix) matrix).androidMatrix);
    }

    public boolean isIdentity() {
        return androidMatrix.isIdentity();
    }
}
