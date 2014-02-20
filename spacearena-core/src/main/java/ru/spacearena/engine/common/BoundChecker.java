package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded {
        Bounds getBounds();
        void onOutOfBounds(float dx, float dy);
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
        final float dx = computeAxisOffset(bounds.getMinX(), bounds.getMaxX(),
                object.getBounds().getMinX(), object.getBounds().getMaxX());
        final float dy = computeAxisOffset(bounds.getMinY(), bounds.getMaxY(),
                object.getBounds().getMinY(), object.getBounds().getMaxY());
        if (!FloatMathUtils.isZero(dx, dy)) {
            object.onOutOfBounds(dx, dy);
        }
        return true;
    }
}
