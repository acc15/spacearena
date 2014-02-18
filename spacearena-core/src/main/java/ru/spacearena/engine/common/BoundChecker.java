package ru.spacearena.engine.common;

import ru.spacearena.engine.AABB;
import ru.spacearena.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded {
        float getMinX();
        float getMaxX();
        float getMinY();
        float getMaxY();
        void offset(float dx, float dy);
    }

    private AABB bounds;
    private Bounded object;

    public BoundChecker(AABB bounds, Bounded object) {
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
        final float dx = computeAxisOffset(bounds.minX, bounds.maxX, object.getMinX(), object.getMaxX());
        final float dy = computeAxisOffset(bounds.minY, bounds.maxY, object.getMinY(), object.getMaxY());
        object.offset(dx, dy);
        return true;
    }
}
