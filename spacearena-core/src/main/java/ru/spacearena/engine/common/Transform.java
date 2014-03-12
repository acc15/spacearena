package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.BitUtils;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Transform extends GenericContainer {

    private static final int MATRIX_DIRTY_MASK = 0x01;
    private static final int ROTATE_DIRTY_MASK = 0x06;

    private static final int MATRIX_DIRTY_VALUE = 1;
    private static final int ANGLE_DIRTY_VALUE = 1 << 1;
    private static final int SINCOS_DIRTY_VALUE = 2 << 1;

    Matrix localSpace;
    Matrix worldSpace;

    float x = 0f, y = 0f;
    float scaleX = 1f, scaleY = 1f;
    float skewX = 0f, skewY = 0f;
    float pivotX = 0f, pivotY = 0f;
    float rotate = 0f;
    float rotateX = 0f, rotateY = 0f;

    int dirty = 0;

    private void markMatrixDirty() {
        this.dirty = BitUtils.set(dirty, MATRIX_DIRTY_MASK, MATRIX_DIRTY_VALUE);
    }

    private void resetMatrixDirty() {
        this.dirty = BitUtils.reset(dirty, MATRIX_DIRTY_MASK);
    }

    private void resetRotateDirty() {
        this.dirty = BitUtils.reset(dirty, ROTATE_DIRTY_MASK);
    }

    private void markRotateDirty(boolean angle) {
        this.dirty = BitUtils.set(dirty, MATRIX_DIRTY_MASK | ROTATE_DIRTY_MASK,
                MATRIX_DIRTY_VALUE | (angle ? ANGLE_DIRTY_VALUE : SINCOS_DIRTY_VALUE));
    }

    private boolean isMatrixDirty() {
        return BitUtils.get(dirty, MATRIX_DIRTY_MASK) == MATRIX_DIRTY_VALUE;
    }

    private boolean isAngleDirty() {
        return BitUtils.get(dirty, ROTATE_DIRTY_MASK) == ANGLE_DIRTY_VALUE;
    }

    private boolean isSinCosDirty() {
        return BitUtils.get(dirty, ROTATE_DIRTY_MASK) == SINCOS_DIRTY_VALUE;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        markMatrixDirty();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        markMatrixDirty();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        markMatrixDirty();
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        markMatrixDirty();
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        markMatrixDirty();
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        markMatrixDirty();
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
        markMatrixDirty();
    }

    public float getSkewY() {
        return skewY;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
        markMatrixDirty();
    }

    public void setSkew(float skewX, float skewY) {
        this.skewX = skewX;
        this.skewY = skewY;
        markMatrixDirty();
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
        markMatrixDirty();
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
        markMatrixDirty();
    }

    public void setPivot(float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        markMatrixDirty();
    }

    private void updateAngleIfNeeded() {
        if (!isAngleDirty()) {
            return;
        }
        this.rotate = FloatMathUtils.toDegrees(FloatMathUtils.atan2(this.rotateY, this.rotateX));
        resetRotateDirty();
    }

    private void updateSinCosIfNeeded() {
        if (!isSinCosDirty()) {
            return;
        }
        final float rads = FloatMathUtils.toRadians(rotate);
        this.rotateX = FloatMathUtils.cos(rads);
        this.rotateY = FloatMathUtils.sin(rads);
        resetRotateDirty();
    }

    public float getRotate() {
        updateAngleIfNeeded();
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = FloatMathUtils.normalizeDegrees(rotate);
        markRotateDirty(false);
    }

    public void setRotate(float cos, float sin) {
        this.rotateX = cos;
        this.rotateY = sin;
        markRotateDirty(true);
    }

    public float getRotateX() {
        updateSinCosIfNeeded();
        return rotateX;
    }

    public float getRotateY() {
        updateSinCosIfNeeded();
        return rotateY;
    }

    public void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
        markMatrixDirty();
    }

    protected void updateMatrices() {
        if (!isMatrixDirty()) {
            return;
        }
        resetMatrixDirty();
        updateSinCosIfNeeded();
        worldSpace.set(pivotX, pivotY, scaleX, scaleY, skewX, skewY, rotateX, rotateY, x, y);
        localSpace.inverse(worldSpace);
        onMatrixUpdate();
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
