package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
public class Rect2FPP extends AbstractRect2F {

    public float x1, y1, x2, y2;

    public Rect2FPP() {
    }

    public Rect2FPP(float x1, float y1, float x2, float y2) {
        set(x1, y1, x2, y2);
    }

    public Rect2FPP(BoundingBox2F aabb) {
        set(aabb);
    }

    public void set(BoundingBox2F bb) {
        set(bb.getMinX(), bb.getMinY(), bb.getMaxX(), bb.getMaxY());
    }

    public void set(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void offset(float dx, float dy) {
        this.x1 += dx;
        this.y1 += dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    public float getCenterX() {
        return FloatMathUtils.center(x1, x2);
    }

    public float getCenterY() {
        return FloatMathUtils.center(y1, y2);
    }

    public float getWidth() {
        return FloatMathUtils.size(x1, x2);
    }

    public float getHeight() {
        return FloatMathUtils.size(y1, y2);
    }

    public float getHalfWidth() {
        return FloatMathUtils.halfSize(x1, x2);
    }

    public float getHalfHeight() {
        return FloatMathUtils.halfSize(y1, y2);
    }

    public void moveTo(float x, float y) {
        final float cx = getHalfWidth(), cy = getHalfHeight();
        this.x1 = x - cx;
        this.x2 = x + cx;
        this.y1 = y - cy;
        this.y2 = y + cy;
    }

    public void scale(float scaleX, float scaleY) {
        final float cx = getWidth(), cy = getHeight();
        inflate((cx*scaleX-cx)/2, (cy*scaleY-cy)/2);
    }

    public void inflate(float dx, float dy) {
        this.x1 -= dx;
        this.y1 -= dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    public void extend(float dx, float dy) {
        if (dx > 0) {
            x2 += dx;
        } else {
            x1 += dx;
        }
        if (dy > 0) {
            y2 += dy;
        } else {
            y1 += dy;
        }
    }

    public void computeBoundingBox(float[] pts, int start, int pointCount) {
        for (int i=0; i<pointCount; i++) {
            final float x = pts[i*2+start], y = pts[i*2+start+1];
            if (x < x1) {
                x1 = x;
            } else if (x > x2) {
                x2 = x;
            }
            if (y < y1) {
                y1 = y;
            } else if (y > y2) {
                y2 = y;
            }
        }
    }

    @Override
    public float getPointX(int i) {
        return i / 2 % 2 == 0 ? x1 : x2;
    }

    @Override
    public float getPointY(int i) {
        return (i+1) / 2 % 2 == 0 ? y1 : y2;
    }

    @Override
    public int getPointCount() {
        return 4;
    }

    public float getMinX() {
        return x1;
    }

    public float getMaxX() {
        return x2;
    }

    public float getMinY() {
        return y1;
    }

    public float getMaxY() {
        return y2;
    }

}
