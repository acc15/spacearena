package ru.spacearena.engine.common;

import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class Transform extends AbstractTransformation {

    float x = 0f, y = 0f;
    float scaleX = 1f, scaleY = 1f;
    float skewX = 0f, skewY = 0f;
    float pivotX = 0f, pivotY = 0f;
    float rotation = 0f;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        markDirty();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        markDirty();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        markDirty();
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        markDirty();
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        markDirty();
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        markDirty();
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
        markDirty();
    }

    public float getSkewY() {
        return skewY;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
        markDirty();
    }

    public void setSkew(float skewX, float skewY) {
        this.skewX = skewX;
        this.skewY = skewY;
        markDirty();
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
        markDirty();
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
        markDirty();
    }

    public void setPivot(float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        markDirty();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void applyTransformations(Matrix matrix) {
        matrix.translate(x, y);
        matrix.rotate(rotation);
        matrix.skew(skewX, skewY);
        matrix.scale(scaleX, scaleY);
        matrix.translate(-pivotX, -pivotY);
    }

}
