package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Matrix;
import ru.spacearena.engine.handlers.DrawHandler;
import ru.spacearena.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class TransformHandler implements DrawHandler {

    private final Matrix matrix = new Matrix();
    private final Matrix concatMatrix = new Matrix();
    private Matrix canvasMatrix = null;

    float x = 0f, y = 0f;
    float scaleX = 1f, scaleY = 1f;
    float skewX = 0f, skewY = 0f;
    float rotationCenterX = 0f, rotationCenterY = 0f;
    float scaleCenterX = 0f, scaleCenterY = 0f;
    float skewCenterX = 0f, skewCenterY = 0f;
    float rotation = 0f;
    boolean isDirty = false;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        this.isDirty = true;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        this.isDirty = true;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.isDirty = true;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        this.isDirty = true;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        this.isDirty = true;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.isDirty = true;
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
        this.isDirty = true;
    }

    public float getSkewY() {
        return skewY;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
        this.isDirty = true;
    }

    public void setSkew(float skewX, float skewY) {
        this.skewX = skewX;
        this.skewY = skewY;
        this.isDirty = true;
    }

    public float getRotationCenterX() {
        return rotationCenterX;
    }

    public void setRotationCenterX(float rotationCenterX) {
        this.rotationCenterX = rotationCenterX;
        this.isDirty = true;
    }

    public float getRotationCenterY() {
        return rotationCenterY;
    }

    public void setRotationCenterY(float rotationCenterY) {
        this.rotationCenterY = rotationCenterY;
        this.isDirty = true;
    }

    public void setRotationCenter(float x, float y) {
        this.rotationCenterX = x;
        this.rotationCenterY = y;
        this.isDirty = true;
    }

    public float getScaleCenterX() {
        return scaleCenterX;
    }

    public void setScaleCenterX(float scaleCenterX) {
        this.scaleCenterX = scaleCenterX;
        this.isDirty = true;
    }

    public float getScaleCenterY() {
        return scaleCenterY;
    }

    public void setScaleCenterY(float scaleCenterY) {
        this.scaleCenterY = scaleCenterY;
        this.isDirty = true;
    }

    public void setScaleCenter(float x, float y) {
        this.scaleCenterX = x;
        this.scaleCenterY = y;
        this.isDirty = true;
    }

    public float getSkewCenterX() {
        return skewCenterX;
    }

    public void setSkewCenterX(float skewCenterX) {
        this.skewCenterX = skewCenterX;
        this.isDirty = true;
    }

    public float getSkewCenterY() {
        return skewCenterY;
    }

    public void setSkewCenterY(float skewCenterY) {
        this.skewCenterY = skewCenterY;
        this.isDirty = true;
    }

    public void setSkewCenter(float x, float y) {
        this.skewCenterX = x;
        this.skewCenterY = y;
        this.isDirty = true;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setPivotX(float x) {
        this.rotationCenterX = x;
        this.scaleCenterX = x;
        this.skewCenterX = x;
        this.isDirty = true;
    }

    public void setPivotY(float y) {
        this.rotationCenterY = y;
        this.scaleCenterY = y;
        this.skewCenterY = y;
        this.isDirty = true;
    }

    public void setPivot(float x, float y) {
        this.rotationCenterX = x;
        this.rotationCenterY = y;
        this.scaleCenterX = x;
        this.scaleCenterY = y;
        this.skewCenterX = x;
        this.skewCenterY = y;
        this.isDirty = true;
    }

    private Matrix getMatrix() {
        if (FloatMathUtils.isZero(rotation) &&
            FloatMathUtils.isOne(scaleX, scaleY) &&
            FloatMathUtils.isZero(skewX, skewY) &&
            FloatMathUtils.isZero(x, y)) {
            return null;
        }
        if (!isDirty) {
            return matrix;
        }
        matrix.reset();
        matrix.postRotate(rotation, rotationCenterX, rotationCenterY);
        matrix.postScale(scaleX, scaleY, scaleCenterX, scaleCenterY);
        matrix.postSkew(skewX, skewY, skewCenterX, skewCenterY);
        matrix.postTranslate(x, y);
        isDirty = false;
        return matrix;
    }

    public void mapPoints(float[] pts) {
        final Matrix mx = getMatrix();
        if (mx != null) {
            mx.mapPoints(pts);
        }
    }

    public void onDraw(Canvas canvas) {
    }

    public void onPreDraw(Canvas canvas) {
        final Matrix thisMatrix = getMatrix();
        if (thisMatrix != null) {
            canvasMatrix = canvas.getMatrix();
            concatMatrix.setConcat(canvasMatrix, thisMatrix);
            canvas.setMatrix(concatMatrix);
        }
    }

    public void onPostDraw(Canvas canvas) {
        if (canvasMatrix != null) {
            canvas.setMatrix(canvasMatrix);
            canvasMatrix = null;
        }
    }
}
