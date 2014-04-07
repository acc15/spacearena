package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends Transform<EngineEntity> implements BoundChecker.Bounded {

    private final ViewportAdjustStrategy adjustStrategy;
    private final Rect2FPP bounds = new Rect2FPP();
    private Matrix localSpace = new Matrix();

    public Viewport() {
        this(new DefaultAdjustStrategy());
    }

    public Viewport(ViewportAdjustStrategy adjustStrategy) {
        this.adjustStrategy = adjustStrategy;
    }

    public void onOutOfBounds(float dx, float dy) {
        translate(dx, dy);
    }

    public static interface ViewportAdjustStrategy {
        void adjustViewport(float width, float height, Transform<?> tx);
    }

    public static class DefaultAdjustStrategy implements ViewportAdjustStrategy {
        public void adjustViewport(float width, float height, Transform<?> tx) {
        }
    }

    public static class RealSizeAdjustStrategy implements ViewportAdjustStrategy {
        public void adjustViewport(float width, float height, Transform<?> tx) {
            final float hw = width / 2, hh = height / 2;
            tx.setScale(hw, -hh);
            tx.setPosition(hw, hh);
        }
    }

    public static class LargestSideAdjustStrategy implements ViewportAdjustStrategy {

        private float largestSize;

        public LargestSideAdjustStrategy(float largestSize) {
            this.largestSize = largestSize;
        }

        public void adjustViewport(float width, float height, Transform<?> tx) {
            // s0 - current scale
            // s1 - new scale (need to compute)
            // d0 - old size (width or height)
            // d1 - new dimension (width or height)
            // r - largestSize
            //
            //      f * r
            // s0 = -----
            //        w0
            //
            //     s0 * w0
            // f = -------
            //        r
            //
            //      f * r
            // s1 = -----
            //        d1
            //
            //      s0 * d0
            //      ------- * r
            //         r
            // s1 = ----------
            //          d1
            //
            //      s0 * d0
            // s1 = -------
            //         d1

            final float scale = largestSize/2;
            if (width > height) {
                tx.setScale(scale, -height/width*scale);
            } else {
                tx.setScale(width/height*scale, -scale);
            }
        }

    }

    @Override
    public void onSize(float width, float height) {
        super.onSize(width, height);
        adjustStrategy.adjustViewport(width, height, this);
    }

    public BoundingBox2F getBounds() {
        updateMatrixIfNeeded();
        return bounds;
    }

    @Override
    protected void onMatrixUpdate() {
        localSpace.inverse(getWorldSpace());
        bounds.set(-1, 1, 1, -1);
        ShapeUtils.computeBoundingBox(bounds, bounds, getWorldSpace());
    }

    public Matrix getLocalSpace() {
        updateMatrixIfNeeded();
        return localSpace;
    }

    @Override
    public Matrix getViewMatrix() {
        return getLocalSpace();
    }

}
