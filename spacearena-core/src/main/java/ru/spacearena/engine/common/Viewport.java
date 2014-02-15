package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends AbstractTransformation<Viewport> {

    private Transform chaseObject;

    private float width, height;

    private ViewportResolutionStrategy resolutionStrategy;

    public Viewport() {
        this(new DefaultResolutionStrategy());
    }

    public Viewport(ViewportResolutionStrategy resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }

    public static interface ViewportResolutionStrategy {
        void resolveDimension(float width, float height, Viewport viewport);
    }

    public static class DefaultResolutionStrategy implements ViewportResolutionStrategy {
        public void resolveDimension(float width, float height, Viewport viewport) {
        }
    }

    public static class LargestSideResolutionStrategy implements ViewportResolutionStrategy {

        private float largestDimension;

        public LargestSideResolutionStrategy(float largestDimension) {
            this.largestDimension = largestDimension;
        }

        public void resolveDimension(float width, float height, Viewport viewport) {
            final float scale = width > height ? width / largestDimension : height / largestDimension;
            viewport.setScale(scale, scale);
        }
    }

    @Override
    public void onInit(Engine engine) {
        super.onInit(engine);
        onSize(engine.getWidth(), engine.getHeight());
    }

    @Override
    public void onSize(float width, float height) {
        super.onSize(width, height);
        this.width = width;
        this.height = height;
        this.resolutionStrategy.resolveDimension(width, height, this);
        this.markDirty();
    }

    public Transform getChaseObject() {
        return chaseObject;
    }

    public void setChaseObject(Transform chaseObject) {
        this.chaseObject = chaseObject;
    }

    public void lookAt(float x, float y) {
        this.x = x;
        this.y = y;
        markDirty();
    }

    protected void applyTransformations(Matrix matrix) {
        matrix.translate(width/2, height/2);
        matrix.rotate(rotation);
        matrix.skew(skewX, skewY);
        matrix.scale(scaleX, scaleY);
        matrix.translate(-x, -y);
        //matrix.rotate(rotation);
    }

    @Override
    public boolean onUpdate(float seconds) {
        super.onUpdate(seconds);
        if (chaseObject != null) {
            lookAt(chaseObject.x, chaseObject.y);
        }
        return true;
    }
}
