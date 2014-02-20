package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends AbstractBoundedTransform implements BoundChecker.Bounded {

    ViewportAdjustStrategy adjustStrategy;
    Matrix inverseMatrix;

    final Bounds originalBounds = new Bounds() {
        public float getMinX() {
            return 0;
        }

        public float getMaxX() {
            return engine.getWidth();
        }

        public float getMinY() {
            return 0;
        }

        public float getMaxY() {
            return engine.getHeight();
        }
    };

    public Viewport() {
        this(new DefaultAdjustStrategy());
    }

    public Viewport(ViewportAdjustStrategy adjustStrategy) {
        this.adjustStrategy = adjustStrategy;
    }

    public Bounds getBounds() {
        return getTransformedBounds();
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
        inverseMatrix = engine.createMatrix();
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

    protected void calculateViewMatrix(Matrix matrix) {
        super.calculateViewMatrix(matrix);
        inverseMatrix.inverse(matrix);
    }

    @Override
    public Matrix getViewMatrix() {
        updateMatrices();
        return inverseMatrix;
    }

    @Override
    public Bounds getOriginalBounds() {
        return originalBounds;
    }
}
