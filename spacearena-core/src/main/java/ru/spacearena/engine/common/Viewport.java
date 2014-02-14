package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public class Viewport extends AbstractTransformation {

    private Matrix matrix;
    private Transform chaseObject;

    private float minX, maxX, minY, maxY;
    private float scaleX, scaleY;
    private float skewX, skewY;
    private float rotation;



    @Override
    public void onInit(Engine engine) {
        this.matrix = engine.createMatrix();
    }

    @Override
    public void onSize(float width, float height) {

    }

    public Transform getChaseObject() {
        return chaseObject;
    }

    public void setChaseObject(Transform chaseObject) {
        this.chaseObject = chaseObject;
    }

    public void lookAt(float x, float y) {

    }

    public float getCenterX() {
        return (maxX - minX) / 2;
    }

    public float getCenterY() {
        return (maxY - minY) / 2;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    protected void applyTransformations(Matrix matrix) {
        matrix.translate(-minX, -minY);
        matrix.rotate(rotation);
        matrix.skew(1/skewX, 1/skewY);
        matrix.scale(1/scaleX, 1/scaleY);

    }

    @Override
    public void onDraw(DrawContext context) {
        if (matrix.isIdentity()) {
            super.onDraw(context);
        }
    }

    @Override
    public boolean onUpdate(float seconds) {
        lookAt(chaseObject.x, chaseObject.y);
        return true;
    }
}
