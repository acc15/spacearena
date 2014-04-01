package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
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
        void initViewport(float width, float height, Transform<?> tx);
        void adjustViewport(float width, float height, Transform<?> tx);
    }

    public static class DefaultAdjustStrategy implements ViewportAdjustStrategy {
        public void adjustViewport(float width, float height, Transform<?> tx) {
        }

        public void initViewport(float width, float height, Transform<?> tx) {
        }
    }

    public static class LargestSideAdjustStrategy implements ViewportAdjustStrategy {

        private float largestRatio;

        public LargestSideAdjustStrategy(float largestRatio) {
            this.largestRatio = largestRatio;
        }

        public void initViewport(float width, float height, Transform<?> tx) {
            tx.setPivotToCenter(width, height);
            tx.setScale(largestRatio / (width > height ? width : height));
        }

        public void adjustViewport(float width, float height, Transform<?> tx) {
            tx.setPivotToCenter(width, height);

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
            final float w0 = tx.getEngine().getWidth(), h0 = tx.getEngine().getHeight();
            final float s = w0 > h0 ? tx.getScaleX() * w0 : tx.getScaleY() * h0;
            tx.setScale(s / FloatMathUtils.max(width, height));
        }

    }

    @Override
    public void onAttach(Engine engine) {
        super.onAttach(engine);
        adjustStrategy.initViewport(engine.getWidth(), engine.getHeight(), this);
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
