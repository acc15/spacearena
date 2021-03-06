package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Rect2FR extends AbstractRect2F {

    public final Point2F position = new Point2F();
    public final Point2F halfSize = new Point2F();

    public Rect2FR() {
    }

    public Rect2FR(Rect2F aabb) {
        set(aabb);
    }

    public Rect2FR(float x, float y, float rx, float ry) {
        set(x, y, rx, ry);
    }

    public void set(Rect2F aabb) {
        set(aabb.getCenterX(), aabb.getCenterY(), aabb.getHalfWidth(), aabb.getHalfHeight());
    }

    public void setBounds(float l, float t, float r, float b) {
        this.position.set(FloatMathUtils.center(l,r), FloatMathUtils.center(t,b));
        this.halfSize.set(FloatMathUtils.halfSize(l,r), FloatMathUtils.halfSize(t,b));
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
