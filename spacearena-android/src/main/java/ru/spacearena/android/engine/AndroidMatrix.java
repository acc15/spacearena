package ru.spacearena.android.engine;

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

    public void multiply(Matrix transform) {
        androidMatrix.preConcat(((AndroidMatrix) transform).androidMatrix);
    }

    public void mapPoints(float[] pts) {
        androidMatrix.mapPoints(pts);
    }

    public boolean inverse(Matrix matrix) {
        return ((AndroidMatrix) matrix).androidMatrix.invert(androidMatrix);
    }

    public boolean isIdentity() {
        return androidMatrix.isIdentity();
    }

    public void set(float pivotX, float pivotY, float scaleX, float scaleY, float skewX, float skewY, float rotateX, float rotateY, float x, float y) {
        androidMatrix.setSinCos(rotateX, rotateY);
        androidMatrix.preSkew(skewX, skewY);
        androidMatrix.preScale(scaleX, scaleY);
        androidMatrix.preTranslate(-pivotX, -pivotY);
        androidMatrix.postTranslate(x, y);
    }
}
