package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geom.Bounds;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded extends Bounds {
        void offset(float dx, float dy);
    }

    private Bounds bounds;
    private Bounded object;

    public BoundChecker(Bounds bounds, Bounded object) {
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
        final float dx = computeAxisOffset(bounds.getMinX(), bounds.getMaxX(), object.getMinX(), object.getMaxX());
        final float dy = computeAxisOffset(bounds.getMinY(), bounds.getMaxY(), object.getMinY(), object.getMaxY());
        object.offset(dx, dy);
        return true;
    }
}
