package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geometry.shapes.Quad2F;
import ru.spacearena.engine.geometry.shapes.Rect2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends Transform<EngineEntity> implements BoundChecker.Bounded {

    private final ViewportAdjustStrategy adjustStrategy;
    private final Quad2F window = new Quad2F();
    private final Rect2F bounds = new Rect2FP();
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
            tx.setPivot(-1, 1);
            tx.setScale(width/2, -height/2);
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

    public Rect2F getBounds() {
        updateMatrixIfNeeded();
        return bounds;
    }

    public Quad2F getWindow() {
        updateMatrixIfNeeded();
        return window;
    }

    @Override
    protected void onMatrixUpdate() {
        localSpace.inverse(getWorldSpace());
        window.setRect(-1, 1, 1, -1);
        window.transform(getWorldSpace());
        bounds.computeBoundingBox(window);
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
