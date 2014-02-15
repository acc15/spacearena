package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends Transform {

    ViewportAdjustStrategy adjustStrategy;
    Matrix inverseMatrix;

    public Viewport() {
        this(new DefaultAdjustStrategy());
    }

    public Viewport(ViewportAdjustStrategy adjustStrategy) {
        this.adjustStrategy = adjustStrategy;
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
        inverseMatrix = engine.createMatrix();
        adjustViewport(engine.getWidth(), engine.getHeight());
        super.onInit(engine);
    }

    void adjustViewport(float width, float height) {
        setPivot(width / 2, height / 2);
        this.adjustStrategy.adjustViewport(width, height, this);
    }

    @Override
    public void onSize(float width, float height) {
        super.onSize(width, height);
        adjustViewport(width, height);
    }

    protected Matrix applyTransformations(Matrix matrix) {
        super.applyTransformations(matrix);
        inverseMatrix.inverse(matrix);
        return inverseMatrix;
    }

}
