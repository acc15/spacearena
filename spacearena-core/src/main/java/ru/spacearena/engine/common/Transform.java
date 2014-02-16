package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Transform extends GenericContainer {

    Matrix matrix;
    boolean isDirty = false;

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
        markDirty();
    }

    private void calculateMatrix() {
        matrix.identity();
        matrix.translate(x, y);
        matrix.rotate(rotation);
        matrix.skew(skewX, skewY);
        matrix.scale(scaleX, scaleY);
        matrix.translate(-pivotX, -pivotY);
    }

    protected void calculateViewMatrix(Matrix matrix) {
    }

    public void markDirty() {
        this.isDirty = true;
    }

    protected void updateMatrices() {
        if (isDirty) {
            calculateMatrix();
            calculateViewMatrix(matrix);
            isDirty = false;
        }
    }

    public Matrix getViewMatrix() {
        updateMatrices();
        return matrix;
    }

    public Matrix getMatrix() {
        updateMatrices();
        return matrix;
    }

    public void mapPoints(float[] pts) {
        getMatrix().mapPoints(pts);
    }

    public void onInit(Engine engine) {
        this.matrix = engine.createMatrix();
        super.onInit(engine);
    }

    @Override
    public void onDraw(DrawContext context) {
        final Matrix thisMatrix = getViewMatrix();
        if (thisMatrix.isIdentity()) {
            super.onDraw(context);
            return;
        }
        final Matrix oldMatrix = context.getMatrixCopy();
        final Matrix concatMatrix = context.getMatrixCopy();
        concatMatrix.multiply(thisMatrix);
        try {
            context.setMatrix(concatMatrix);
            super.onDraw(context);
        } finally {
            context.setMatrix(oldMatrix);
        }
    }

}
