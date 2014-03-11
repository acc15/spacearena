package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Transform extends GenericContainer {

    Matrix localSpace;
    Matrix worldSpace;
    boolean isDirty = false;

    float x = 0f, y = 0f;
    float scaleX = 1f, scaleY = 1f;
    float skewX = 0f, skewY = 0f;
    float pivotX = 0f, pivotY = 0f;
    float angle = 0f;

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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = FloatMathUtils.normalizeDegrees(angle);
        markDirty();
    }

    public void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
        isDirty = true;
    }

    public void markDirty() {
        this.isDirty = true;
    }

    protected void updateMatrices() {
        if (isDirty) {
            isDirty = false;
            worldSpace.identity();
            worldSpace.translate(x, y);
            worldSpace.rotate(angle);
            worldSpace.skew(skewX, skewY);
            worldSpace.scale(scaleX, scaleY);
            worldSpace.translate(-pivotX, -pivotY);
            localSpace.inverse(worldSpace);
            onMatrixUpdate();
        }
    }

    protected void onMatrixUpdate() {
    }

    public Matrix getViewMatrix() {
        return getWorldSpace();
    }

    public Matrix getLocalSpace() {
        updateMatrices();
        return localSpace;
    }

    public Matrix getWorldSpace() {
        updateMatrices();
        return worldSpace;
    }

    public void mapPoints(float[] pts) {
        getWorldSpace().mapPoints(pts);
    }

    public void onInit(Engine engine) {
        this.worldSpace = engine.createMatrix();
        this.localSpace = engine.createMatrix();
        super.onInit(engine);
    }

    @Override
    public void onDraw(DrawContext context) {
        final Matrix viewMatrix = getViewMatrix();
        if (viewMatrix.isIdentity()) {
            super.onDraw(context);
            return;
        }
        final Matrix oldMatrix = context.getMatrixCopy();
        final Matrix concatMatrix = context.getMatrixCopy();
        concatMatrix.multiply(viewMatrix);
        try {
            context.setMatrix(concatMatrix);
            super.onDraw(context);
        } finally {
            context.setMatrix(oldMatrix);
        }
    }

}
