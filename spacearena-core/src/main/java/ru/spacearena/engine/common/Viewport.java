package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;
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

    public static class LargestSideAdjustStrategy implements ViewportAdjustStrategy {

        private float largestRatio;
        private float prevWidth = -1, prevHeight = -1;

        public LargestSideAdjustStrategy(float largestRatio) {
            this.largestRatio = largestRatio;
        }

        public void adjustViewport(float width, float height, Transform<?> tx) {
            // s0 - current scale
            // s1 - new scale (need to compute)
            // d0 - old size (width or height)
            // d1 - new dimension (width or height)
            // r - largestRatio
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

            if (prevWidth < 0 || prevHeight < 0) {
                tx.setScale(largestRatio / (width > height ? width : height));
            } else {
                final float s = prevWidth > prevHeight ? tx.getScaleX() * prevWidth : tx.getScaleY() * prevHeight;
                tx.setScale(s / FloatMathUtils.max(width, height));
            }
            prevWidth = width;
            prevHeight = height;
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
