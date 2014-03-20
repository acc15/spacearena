package ru.spacearena.android.engine;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;

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

    public void mapPoints(float[] dst, int dstOffset, float[] src, int srcOffset, int pointCount) {
        androidMatrix.mapPoints(dst, dstOffset, src, srcOffset, pointCount);
    }

    public void mapPoint(Point2F point) {
        mapPoint(point, point);
    }

    public void mapPoint(Point2F point, Point2F out) {
        androidMatrix.mapPoints(TempUtils.tempBuf(point.x, point.y));
        out.set(TempUtils.POINT_BUF[0], TempUtils.POINT_BUF[1]);
    }

    public boolean inverse(Matrix matrix) {
        return ((AndroidMatrix) matrix).androidMatrix.invert(androidMatrix);
    }

    public boolean isIdentity() {
        return androidMatrix.isIdentity();
    }

    public void set(float pivotX, float pivotY, float scaleX, float scaleY, float skewX, float skewY, float rotateX, float rotateY, float x, float y) {
        androidMatrix.setSinCos(rotateY, rotateX);
        androidMatrix.postTranslate(x, y);
        androidMatrix.preSkew(skewX, skewY);
        androidMatrix.preScale(scaleX, scaleY);
        androidMatrix.preTranslate(-pivotX, -pivotY);
    }

    public void set(float pivotX, float pivotY, float scaleX, float scaleY, float skewX, float skewY, float rotation, float x, float y) {
        androidMatrix.reset();
        androidMatrix.preTranslate(x, y);
        androidMatrix.preRotate(rotation);
        androidMatrix.preSkew(skewX, skewY);
        androidMatrix.preScale(scaleX, scaleY);
        androidMatrix.preTranslate(-pivotX, -pivotY);
    }

    public void set(float rotateX, float rotateY, float x, float y) {
        androidMatrix.setSinCos(rotateY, rotateX);
        androidMatrix.postTranslate(x, y);
    }

    public void set(float x, float y, float radians) {
        androidMatrix.setRotate(FloatMathUtils.toDegrees(radians));
        androidMatrix.postTranslate(x, y);
    }
}
