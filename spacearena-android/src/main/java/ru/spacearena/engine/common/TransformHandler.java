package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Matrix;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.handlers.DrawHandler;
import ru.spacearena.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class TransformHandler implements DrawHandler {

    private Matrix matrix = new Matrix();
    private Matrix canvasMatrix = new Matrix();
    private Matrix concatMatrix = new Matrix();

    private Point2F translate = Point2F.ZERO;
    private Point2F scale = Point2F.ONE;
    private Point2F skew = Point2F.ZERO;
    private Point2F rotationCenter = Point2F.ZERO;
    private Point2F scaleCenter = Point2F.ZERO;
    private Point2F skewCenter = Point2F.ZERO;
    private float rotation = 0f;
    private boolean isDirty = false;

    public Point2F getTranslate() {
        return translate;
    }

    public void setTranslate(Point2F translate) {
        this.translate = translate;
        this.isDirty = true;
    }

    public Point2F getScale() {
        return scale;
    }

    public void setScale(Point2F scale) {
        this.scale = scale;
        this.isDirty = true;
    }

    public Point2F getSkew() {
        return skew;
    }

    public void setSkew(Point2F skew) {
        this.skew = skew;
        this.isDirty = true;
    }

    public Point2F getRotationCenter() {
        return rotationCenter;
    }

    public void setRotationCenter(Point2F rotationCenter) {
        this.rotationCenter = rotationCenter;
        this.isDirty = true;
    }

    public Point2F getScaleCenter() {
        return scaleCenter;
    }

    public void setScaleCenter(Point2F scaleCenter) {
        this.scaleCenter = scaleCenter;
        this.isDirty = true;
    }

    public Point2F getSkewCenter() {
        return skewCenter;
    }

    public void setSkewCenter(Point2F skewCenter) {
        this.skewCenter = skewCenter;
        this.isDirty = true;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.isDirty = true;
    }

    private Matrix getMatrix() {
        if (!isDirty) {
            return matrix;
        }
        matrix.reset();
        if (!FloatMathUtils.isEqual(rotation, 0f)) {
            matrix.postRotate(rotation, rotationCenter.getX(), rotationCenter.getY());
        }
        if (!scale.isOne()) {
            matrix.postScale(scale.getX(), scale.getY(), scaleCenter.getX(), scaleCenter.getY());
        }
        if (!skew.isZero()) {
            matrix.postSkew(skew.getX(), skew.getY(), skewCenter.getX(), skewCenter.getY());
        }
        if (!translate.isZero()) {
            matrix.postTranslate(translate.getX(), translate.getY());
        }
        isDirty = false;
        return matrix;
    }

    public Point2F mapPoint(Point2F pt) {
        final float[] pts = pt.toFloatArray();
        matrix.mapPoints(pts);
        return Point2F.toPoint(pts);
    }

    public Point2F[] mapPoints(Point2F... points) {
        final float[] pts = Point2F.toFloatArray(points);
        matrix.mapPoints(pts);
        return Point2F.toPointArray(pts);
    }

    public void onDraw(Canvas canvas) {
    }

    public void onPreDraw(Canvas canvas) {
        final Matrix thisMatrix = getMatrix();
        canvas.getMatrix(canvasMatrix);
        concatMatrix.setConcat(canvasMatrix, thisMatrix);
        canvas.setMatrix(concatMatrix);
    }

    public void onPostDraw(Canvas canvas) {
        canvas.setMatrix(canvasMatrix);
    }
}
