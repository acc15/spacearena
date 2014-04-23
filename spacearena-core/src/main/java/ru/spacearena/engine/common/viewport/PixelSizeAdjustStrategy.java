package ru.spacearena.engine.common.viewport;

import ru.spacearena.engine.geometry.shapes.Rect2FP;

/**
* @author Vyacheslav Mayorov
* @since 2014-18-04
*/
public class PixelSizeAdjustStrategy implements ViewportAdjustStrategy {
    public void adjustViewport(float width, float height, Viewport tx) {
        final Rect2FP r = tx.getAreaRect();
        tx.setPivot(r.p1.x, r.p1.y);
        tx.setScale(width/r.getWidth(), height/r.getHeight());
    }
}
