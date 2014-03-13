package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;
import ru.vmsoftware.math.geometry.shapes.AABB2F;
import ru.vmsoftware.math.geometry.shapes.Rect2FPP;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends Transform implements BoundChecker.Bounded {

    ViewportAdjustStrategy adjustStrategy;

    private final Rect2FPP bounds = new Rect2FPP();

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
        void adjustViewport(float width, float height, Viewport viewport);
    }

    public static class DefaultAdjustStrategy implements ViewportAdjustStrategy {
        public void adjustViewport(float width, float height, Viewport viewport) {
        }
    }

    public static class LargestSideAdjustStrategy implements ViewportAdjustStrategy {

        private float largestDimension;

        public LargestSideAdjustStrategy(float largestDimension) {
            this.largestDimension = largestDimension;
        }

        public void adjustViewport(float width, float height, Viewport viewport) {
            final float scale = width > height ? largestDimension/width : largestDimension/height;
            viewport.setScale(scale, scale);
        }
    }

    @Override
    public void onInit(Engine engine) {
        super.onInit(engine);
        adjustViewport(engine.getWidth(), engine.getHeight());
    }

    @Override
    public void onSize(float width, float height) {
        super.onSize(width, height);
        adjustViewport(width, height);
    }

    void adjustViewport(float width, float height) {
        setPivot(width / 2, height / 2);
        this.adjustStrategy.adjustViewport(width, height, this);
    }

    public AABB2F getBounds() {
        updateMatricesIfNeeded();
        return bounds;
    }

    @Override
    protected void onMatrixUpdate() {
        bounds.set(0, 0, engine.getWidth(), engine.getHeight());
        ShapeUtils.computeBoundingBox(bounds, bounds, getWorldSpace());
    }

    @Override
    public Matrix getViewMatrix() {
        return getLocalSpace();
    }

}
