package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2F;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-02
 */
public class BoundChecker extends EngineObject {

    public static interface Bounded {
        Rect2F getRect();
        void onOutOfBounds(float dx, float dy);
    }

    private Rect2F bounds;
    private Bounded object;

    public BoundChecker(Rect2F bounds, Bounded object) {
        this.bounds = bounds;
        this.object = object;
    }

    private float computeAxisOffset(float bMin, float bMax, float oMin, float oMax) {
        final float dMin = bMin - oMin;
        final float dMax = bMax - oMax;
        if (dMin > 0) {
            return dMin > dMax ? FloatMathUtils.center(dMin, dMax) : dMin;
        }
        if (dMax < 0) {
            return dMin > dMax ? FloatMathUtils.center(dMin, dMax) : dMax;
        }
        return 0;
    }

    @Override
    public void onUpdate(float seconds) {
        final Rect2F oBounds = object.getRect();
        final float dx = computeAxisOffset(bounds.getMinX(), bounds.getMaxX(), oBounds.getMinX(), oBounds.getMaxX());
        final float dy = computeAxisOffset(bounds.getMinY(), bounds.getMaxY(), oBounds.getMinY(), oBounds.getMaxY());
        if (!FloatMathUtils.isZero(dx, dy)) {
            object.onOutOfBounds(dx, dy);
        }
    }
}
