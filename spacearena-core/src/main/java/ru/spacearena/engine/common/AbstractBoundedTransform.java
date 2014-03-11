package ru.spacearena.engine.common;

import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public abstract class AbstractBoundedTransform extends Transform {

    final AABB bounds = new AABB();
    final float[] boundPoints = new float[8];

    public abstract Bounds getOriginalBounds();

    public Bounds getTransformedBounds() {
        updateMatrices();
        return bounds;
    }

    @Override
    protected void onMatrixUpdate() {
        final Bounds originalBounds = getOriginalBounds();
        ShapeUtils.fillRect(boundPoints,
                originalBounds.getMinX(), originalBounds.getMinY(), originalBounds.getMaxX(), originalBounds.getMaxY());
        mapPoints(boundPoints);
        bounds.calculate(boundPoints);
    }

}
