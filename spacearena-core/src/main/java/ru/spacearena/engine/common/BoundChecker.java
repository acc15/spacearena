package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded {
        BoundingBox2F getBounds();
        void onOutOfBounds(float dx, float dy);
    }

    private BoundingBox2F bounds;
    private Bounded object;

    public BoundChecker(BoundingBox2F bounds, Bounded object) {
        this.bounds = bounds;
        this.object = object;
    }

    private float computeAxisOffset(float bMin, float bMax, float oMin, float oMax) {
        if (oMin < bMin) {
            return bMin - oMin;
        }
        if (oMax > bMax) {
            return bMax - oMax;
        }
        return 0f;
    }

    @Override
    public boolean onUpdate(float seconds) {
        final BoundingBox2F oBounds = object.getBounds();
        final float dx = computeAxisOffset(bounds.getMinX(), bounds.getMaxX(), oBounds.getMinX(), oBounds.getMaxX());
        final float dy = computeAxisOffset(bounds.getMinY(), bounds.getMaxY(), oBounds.getMinY(), oBounds.getMaxY());
        if (!FloatMathUtils.isZero(dx, dy)) {
            object.onOutOfBounds(dx, dy);
        }
        return true;
    }
}
