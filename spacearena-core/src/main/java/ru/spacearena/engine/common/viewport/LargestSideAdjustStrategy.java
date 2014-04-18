package ru.spacearena.engine.common.viewport;

import ru.spacearena.engine.geometry.shapes.Rect2F;
import ru.spacearena.engine.util.FloatMathUtils;

/**
* @author Vyacheslav Mayorov
* @since 2014-18-04
*/
public class LargestSideAdjustStrategy implements ViewportAdjustStrategy {

    private float largestSize;
    private float w0 = -1, h0 = -1;

    public LargestSideAdjustStrategy(float largestSize) {
        this.largestSize = largestSize;
    }

    public void adjustViewport(float width, float height, Viewport tx) {
        final Rect2F r = tx.getAreaRect();
        final float w1 = r.getWidth(), h1 = r.getHeight();
        tx.setPivotToCenter(w1, h1);
        if (w0 < 0 || h0 < 0) {
            tx.setScale(largestSize / (w1 > h1 ? w1 : h1));
        } else {
            final float s = w0 > h0 ? tx.getScaleX() * w0 : tx.getScaleY() * h0;
            tx.setScale(s / FloatMathUtils.max(w1, h1));
        }
        w0 = w1;
        h0 = h1;
    }

}
