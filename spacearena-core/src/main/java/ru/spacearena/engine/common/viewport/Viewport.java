package ru.spacearena.engine.common.viewport;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.common.BoundChecker;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.geometry.shapes.Quad2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends Transform<EngineEntity> implements BoundChecker.Bounded, ViewportArea {

    private final ViewportAdjustStrategy adjustStrategy;
    private final Quad2F quad = new Quad2F();
    private final Rect2FP bounds = new Rect2FP();
    private final Matrix localSpace = new Matrix();
    private final ViewportArea area;

    public Viewport(ViewportArea area) {
        this(area, new DefaultAdjustStrategy());
    }

    public Viewport(ViewportArea area, ViewportAdjustStrategy adjustStrategy) {
        this.adjustStrategy = adjustStrategy;
        this.area = area;
    }

    public void onOutOfBounds(float dx, float dy) {
        translate(dx, dy);
    }

    @Override
    public void onSize(float width, float height) {
        adjustStrategy.adjustViewport(width, height, this);
        super.onSize(width, height);
    }

    public Quad2F getQuad() {
        updateMatrixIfNeeded();
        return quad;
    }

    public Rect2FP getRect() {
        updateMatrixIfNeeded();
        return bounds;
    }

    public Quad2F getAreaQuad() {
        return area.getQuad();
    }

    public Rect2FP getAreaRect() {
        return area.getRect();
    }

    @Override
    protected void onMatrixUpdate() {
        localSpace.inverse(getWorldSpace());
        quad.setQuad(area.getQuad());
        quad.transform(getWorldSpace());
        bounds.computeBoundingBox(quad);
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
