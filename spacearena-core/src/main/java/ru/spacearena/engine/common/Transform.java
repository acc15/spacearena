package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Transform<T extends EngineEntity> extends EngineContainer<T> {

    private static final int MATRIX_DIRTY = 1;

    private Matrix worldSpace;

    private float positionX = 0f, positionY = 0f;
    private float scaleX = 1f, scaleY = 1f;
    private float skewX = 0f, skewY = 0f;
    private float pivotX = 0f, pivotY = 0f;
    private float angle = 0f;

    private int dirty = 0;

    public boolean isDirty(int field) {
        return (dirty & field) != 0;
    }

    public boolean isDirty() {
        return dirty != 0;
    }

    private void markDirty(int field) {
        this.dirty |= field;
    }

    private void resetDirty() {
        this.dirty = 0;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        markDirty(MATRIX_DIRTY);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        markDirty(MATRIX_DIRTY);
    }

    public void setPosition(float x, float y) {
        setPositionX(x);
        setPositionY(y);
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        markDirty(MATRIX_DIRTY);
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        markDirty(MATRIX_DIRTY);
    }

    public void setScale(float scaleX, float scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
        markDirty(MATRIX_DIRTY);
    }

    public float getSkewY() {
        return skewY;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
        markDirty(MATRIX_DIRTY);
    }

    public void setSkew(float skewX, float skewY) {
        setSkewX(skewX);
        setSkewY(skewY);
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
        markDirty(MATRIX_DIRTY);
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
        markDirty(MATRIX_DIRTY);
    }

    public void setPivot(float pivotX, float pivotY) {
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = FloatMathUtils.normalizeDegrees(angle);
        markDirty(MATRIX_DIRTY);
    }

    public void rotate(float d) {
        setAngle(angle + d);
    }

    public void translate(float dx, float dy) {
        setPosition(positionX + dx, positionY + dy);
    }

    protected void onMatrixUpdate() {
    }

    public Matrix getViewMatrix() {
        return getWorldSpace();
    }

    @Override
    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        updateMatrixIfNeeded();
        return true;
    }

    public void onAttach(Engine engine) {
        this.worldSpace = engine.createMatrix();
        super.onAttach(engine);
    }

    @Override
    public void onDraw(DrawContext context) {
        final Matrix viewMatrix = getViewMatrix();
        if (viewMatrix.isIdentity()) {
            onDrawTransformed(context);
            return;
        }
        context.pushMatrix(viewMatrix);
        try {
            onDrawTransformed(context);
        } finally {
            context.popMatrix();
        }
    }

    public Matrix getWorldSpace() {
        updateMatrixIfNeeded();
        return worldSpace;
    }

    protected void onDrawTransformed(DrawContext context) {
        super.onDraw(context);
    }

    protected void updateMatrixIfNeeded() {
        if (!isDirty()) {
            return;
        }
        resetDirty();
        worldSpace.set(pivotX, pivotY, scaleX, scaleY, skewX, skewY, angle, positionX, positionY);
        onMatrixUpdate();
    }

}
