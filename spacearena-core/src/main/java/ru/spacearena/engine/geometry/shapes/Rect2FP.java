package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
public class Rect2FP extends AbstractRect2F {

    public final Point2F p1 = new Point2F();
    public final Point2F p2 = new Point2F();

    public Rect2FP() {
    }

    public Rect2FP(float x1, float y1, float x2, float y2) {
        setBounds(x1, y1, x2, y2);
    }

    public Rect2FP(Rect2F aabb) {
        set(aabb);
    }

    public void set(Rect2F bb) {
        setBounds(bb.getMinX(), bb.getMinY(), bb.getMaxX(), bb.getMaxY());
    }


    public void offset(float dx, float dy) {
        p1.add(dx, dy);
        p2.add(dx, dy);
    }

    public float getCenterX() {
        return FloatMathUtils.center(p1.x, p2.x);
    }

    public float getCenterY() {
        return FloatMathUtils.center(p1.y,p2.y);
    }

    public float getWidth() {
        return FloatMathUtils.size(p1.x, p2.x);
    }

    public float getHeight() {
        return FloatMathUtils.size(p1.y, p2.y);
    }

    public float getHalfWidth() {
        return FloatMathUtils.halfSize(p1.x, p2.x);
    }

    public float getHalfHeight() {
        return FloatMathUtils.halfSize(p1.y, p2.y);
    }

    public void moveTo(float x, float y) {
        final float cx = getHalfWidth(), cy = getHalfHeight();
        p1.set(x - cx, y - cy);
        p2.set(x + cx, y + cy);
    }

    public void scale(float scaleX, float scaleY) {
        final float cx = getWidth(), cy = getHeight();
        inflate((cx*scaleX-cx)/2, (cy*scaleY-cy)/2);
    }

    public void inflate(float dx, float dy) {
        this.p1.sub(dx,dy);
        this.p2.add(dx,dy);
    }

    public void extend(float dx, float dy) {
        (dx > 0 ? p2 : p1).x += dx;
        (dy > 0 ? p2 : p1).y += dy;
    }

    public void setBounds(float l, float t, float r, float b) {
        p1.set(l,t);
        p2.set(r,b);
    }


    @Override
    public float getPointX(int i) {
        return i / 2 % 2 == 0 ? p1.x : p2.x;
    }

    @Override
    public float getPointY(int i) {
        return (i+1) / 2 % 2 == 0 ? p1.y : p2.y;
    }

    @Override
    public int getPointCount() {
        return 4;
    }

    public float getMinX() {
        return p1.x;
    }

    public float getMaxX() {
        return p2.x;
    }

    public float getMinY() {
        return p1.y;
    }

    public float getMaxY() {
        return p2.y;
    }

}
