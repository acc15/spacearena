package ru.spacearena.engine.common;

import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public abstract class AbstractBoundedTransform extends Transform implements BoundChecker.Bounded {

    final AABB bounds = new AABB();
    final float[] boundPoints = new float[8];

    public abstract float getWidth();
    public abstract float getHeight();

    public float getMinX() {
        updateMatrices();
        return bounds.minX;
    }

    public float getMaxX() {
        updateMatrices();
        return bounds.maxX;
    }

    public float getMinY() {
        updateMatrices();
        return bounds.minY;
    }

    public float getMaxY() {
        updateMatrices();
        return bounds.maxY;
    }

    public void offset(float dx, float dy) {
        x += dx;
        y += dy;
        isDirty = true;
    }

    @Override
    protected void calculateViewMatrix(Matrix matrix) {
        super.calculateViewMatrix(matrix);
        ShapeUtils.fillRect(boundPoints, 0, 0, getWidth(), getHeight());
        mapPoints(boundPoints);
        bounds.calculate(boundPoints);
    }
}
