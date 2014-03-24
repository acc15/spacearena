package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Rect2FPR extends AbstractRect2F {

    public Point2F position = new Point2F();
    public Point2F halfSize = new Point2F();

    public Rect2FPR() {
    }

    public Rect2FPR(BoundingBox2F aabb) {
        set(aabb);
    }

    public Rect2FPR(float x, float y, float rx, float ry) {
        set(x, y, rx, ry);
    }

    public void set(BoundingBox2F aabb) {
        set(aabb.getCenterX(), aabb.getCenterY(), aabb.getHalfWidth(), aabb.getHalfHeight());
    }

    public void set(float x, float y, float rx, float ry) {
        this.position.set(x,y);
        this.halfSize.set(rx, ry);
    }

    public float getWidth() {
        return halfSize.x * 2;
    }

    public float getHeight() {
        return halfSize.y * 2;
    }

    public float getHalfWidth() {
        return halfSize.x;
    }

    public float getHalfHeight() {
        return halfSize.y;
    }

    public float getMinX() {
        return position.x - halfSize.x;
    }

    public float getMaxX() {
        return position.x + halfSize.x;
    }

    public float getMinY() {
        return position.y - halfSize.y;
    }

    public float getMaxY() {
        return position.y + halfSize.y;
    }

    public float getCenterX() {
        return position.x;
    }

    public float getCenterY() {
        return position.y;
    }

}
