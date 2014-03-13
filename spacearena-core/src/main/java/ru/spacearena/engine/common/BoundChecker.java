package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.vmsoftware.math.FloatMathUtils;
import ru.vmsoftware.math.geometry.shapes.AABB2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded {
        AABB2F getBounds();
        void onOutOfBounds(float dx, float dy);
    }

    private AABB2F bounds;
    private Bounded object;

    public BoundChecker(AABB2F bounds, Bounded object) {
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
        final AABB2F oBounds = object.getBounds();
        final float dx = computeAxisOffset(bounds.getMinX(), bounds.getMaxX(), oBounds.getMinX(), oBounds.getMinX());
        final float dy = computeAxisOffset(bounds.getMinY(), bounds.getMaxY(), oBounds.getMinY(), oBounds.getMaxY());
        if (!FloatMathUtils.isZero(dx, dy)) {
            object.onOutOfBounds(dx, dy);
        }
        return true;
    }
}
