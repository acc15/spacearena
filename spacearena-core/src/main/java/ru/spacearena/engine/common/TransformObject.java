package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class TransformObject extends EngineContainer<EngineObject> {

    private Matrix matrix;
    private Matrix concatMatrix;
    private Matrix oldMatrix = null;

    float x = 0f, y = 0f;
    float scaleX = 1f, scaleY = 1f;
    float skewX = 0f, skewY = 0f;
    float pivotX = 0f, pivotY = 0f;
    float rotation = 0f;
    boolean isDirty = false;
    boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

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

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
        this.isDirty = true;
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
        this.isDirty = true;
    }

    public void setPivot(float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.isDirty = true;
    }

    public void setRotationCenter(float x, float y) {
        this.pivotX = x;
        this.pivotY = y;
        this.isDirty = true;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Matrix getMatrix() {
        if (FloatMathUtils.isZero(rotation) &&
            FloatMathUtils.isOne(scaleX, scaleY) &&
            FloatMathUtils.isZero(skewX, skewY) &&
            FloatMathUtils.isZero(x, y)) {
            return null;
        }
        if (!isDirty) {
            return matrix;
        }
        matrix.identity();
        matrix.translate(x, y);
        matrix.rotate(rotation);
        matrix.skew(skewX, skewY);
        matrix.scale(scaleX, scaleY);
        isDirty = false;
        return matrix;
    }

    public void mapPoints(float[] pts) {
        final Matrix mx = getMatrix();
        if (mx != null) {
            mx.mapPoints(pts);
        }
    }

    public void onInit(Engine engine) {
        this.concatMatrix = engine.createMatrix();
        this.matrix = engine.createMatrix();
    }

    public void onDraw(DrawContext context) {
    }

    public boolean onPreDraw(DrawContext context) {
        if (!visible) {
            return false;
        }

        final Matrix thisMatrix = getMatrix();
        if (thisMatrix == null) {
            return true;
        }
        oldMatrix = context.getMatrix();
        concatMatrix.identity();
        concatMatrix.multiply(oldMatrix);
        concatMatrix.multiply(thisMatrix);
        context.setMatrix(concatMatrix);
        return true;
    }

    public void onPostDraw(DrawContext context) {
        if (oldMatrix != null) {
            context.setMatrix(oldMatrix);
            oldMatrix = null;
        }
    }
}
