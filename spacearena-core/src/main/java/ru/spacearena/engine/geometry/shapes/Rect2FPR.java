package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Rect2FPR extends AbstractRect2F {

    public float x, y, rx, ry;

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
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
    }

    public void offset(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setHalfWidths(float rx, float ry) {
        this.rx = rx;
        this.ry = ry;
    }

    public void scale(float sx, float sy) {
        rx *= sx;
        ry *= sy;
    }

    public void inflate(float dx, float dy) {
        rx += dx;
        ry += dy;
    }

    public float getWidth() {
        return rx * 2;
    }

    public float getHeight() {
        return ry * 2;
    }

    public float getHalfWidth() {
        return rx;
    }

    public float getHalfHeight() {
        return ry;
    }

    public float getMinX() {
        return x - rx;
    }

    public float getMaxX() {
        return x + rx;
    }

    public float getMinY() {
        return y - ry;
    }

    public float getMaxY() {
        return y + ry;
    }

    public float getCenterX() {
        return x;
    }

    public float getCenterY() {
        return y;
    }

    public void extend(float dx, float dy) {
        final float halfdx = dx / 2;
        rx += FloatMathUtils.abs(halfdx);
        x += halfdx;

        final float halfdy = dy / 2;
        ry += FloatMathUtils.abs(halfdy);
        y += halfdy;
    }

}
