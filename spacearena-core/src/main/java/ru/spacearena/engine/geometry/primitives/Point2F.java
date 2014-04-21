package ru.spacearena.engine.geometry.primitives;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Point2F {

    public static final Point2F PT = new Point2F();

    public float x = 0, y = 0;

    public Point2F() {
    }

    public Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2F(Point2F copy) {
        this(copy.x, copy.y);
    }

    public Point2F copy() {
        return new Point2F(x, y);
    }

    public Point2F x(float x) {
        this.x = x;
        return this;
    }

    public Point2F y(float y) {
        this.y = y;
        return this;
    }

    public Point2F set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point2F set(Point2F pt) {
        this.x = pt.x;
        this.y = pt.y;
        return this;
    }


    public Point2F polar(float angle, float distance) {
        final float rads = FloatMathUtils.toRadians(angle);
        return set(FloatMathUtils.cos(rads) * distance, FloatMathUtils.sin(rads) * distance);
    }

    public Point2F xy(float x, float y) {
        return set(x, y);
    }

    public Point2F add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point2F sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Point2F mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Point2F div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Point2F add(Point2F pt) {
        return add(pt.x, pt.y);
    }

    public Point2F sub(Point2F pt) {
        return sub(pt.x, pt.y);
    }

    public Point2F mul(Point2F pt) {
        return mul(pt.x, pt.y);
    }

    public Point2F div(Point2F pt) {
        return div(pt.x, pt.y);
    }

    public Point2F add(float v) {
        return add(v, v);
    }

    public Point2F sub(float v) {
        return sub(v, v);
    }

    public Point2F mul(float v) {
        return mul(v, v);
    }

    public Point2F div(float v) {
        return div(v, v);
    }

    public Point2F negate() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    public float cross(float x, float y) {
        return FloatMathUtils.cross(this.x, this.y, x, y);
    }

    public float cross(Point2F pt) {
        return FloatMathUtils.cross(x, y, pt.x, pt.y);
    }

    public float dot(float x, float y) {
        return FloatMathUtils.dot(this.x, this.y, x, y);
    }

    public float dot(Point2F pt) {
        return FloatMathUtils.dot(x, y, pt.x, pt.y);
    }

    public Point2F lperp() {
        return set((-y), (x));
    }

    public Point2F rperp() {
        return set((y), (-x));
    }

    public Point2F normalize() {
        return div(length());
    }

    public float lengthSquare() {
        return FloatMathUtils.lengthSquare(x, y);
    }

    public float length() {
        return FloatMathUtils.length(x, y);
    }

    /**
     * Returns angle of vector in degrees.
     * <pre>
     *         | 0
     *        -|-
     * 270 /   |   \ 90
     * ---(----o----)-->
     *     \   |   /  x
     *        -|-
     *       y | 180
     *         V
     * </pre>
     * @return angle of vector in degrees.
     */
    public float angle() {
        return FloatMathUtils.degrees(x, y);
    }

    public float atan2() { return FloatMathUtils.atan2(y, x); }


    public float angle(float x, float y) {
        return FloatMathUtils.angle(this.x, this.y, x, y);
    }

    public float angle(Point2F pt) {
        return FloatMathUtils.angle(x, y, pt.x, pt.y);
    }

    public Point2F rotate(float cos, float sin) {
        return set(FloatMathUtils.cross(x,y,sin,cos), FloatMathUtils.dot(x,y,sin,cos));
    }

    public Point2F resize(float newMagnitude) {
        return mul(newMagnitude / length());
    }

    public Point2F resizeIf(float newMagnitude) {
        if (isZero()) {
            return this;
        }
        return resize(newMagnitude);
    }

    public float cosineOfAngle(float x, float y) {
        return dot(x, y) / (length() * FloatMathUtils.length(x, y));
    }

    public float cosineOfAngle(Point2F pt) {
        return cosineOfAngle(pt.x, pt.y);
    }

    public boolean is(float v) {
        return FloatMathUtils.isEqual(x, y, v, v);
    }

    public boolean isZero() {
        return is(0f);
    }

    public boolean isOne() {
        return is(1f);
    }

    public float min() {
        return Math.min(x, y);
    }

    public float max() {
        return Math.max(x,y);
    }

    public static Point2F p(float x, float y) {
        return new Point2F(x, y);
    }

    public static Point2F temp(float x, float y) { return PT.set(x,y); }

    public static Point2F temp(Point2F pt) { return PT.set(pt); }

    public static float[] toFloatArray(Point2F... points) {
        final float[] pts = new float[points.length*2];
        for (int i=0; i<points.length; i++) {
            points[i].toFloatArray(pts,i*2);
        }
        return pts;
    }

    public void toFloatArray(float[] array, int offset) {
        array[offset] = x;
        array[offset+1] = y;
    }

    public float[] toFloatArray() {
        final float[] a = new float[] {x,y};
        toFloatArray(a, 0);
        return a;
    }

    public static Point2F toPoint(float[] pts) {
        return toPoint(pts, 0);
    }

    public static Point2F toPoint(float[] pts, int offset) {
        return new Point2F(pts[offset], pts[offset + 1]);
    }

    public static Point2F[] toPointArray(float... pts) {
        final Point2F[] points = new Point2F[pts.length/2];
        for (int i=0; i<points.length; i++) {
            points[i] = toPoint(pts, i*2);
        }
        return points;
    }

    @Override
    public int hashCode() {
        return Float.floatToRawIntBits(x) ^ Float.floatToRawIntBits(y);
    }

    public boolean near(Point2F pt, float epsilon) { return near(pt.x, pt.y, epsilon); }

    public boolean near(Point2F pt) { return near(pt.x, pt.y); }

    public boolean near(float x, float y, float epsilon) { return FloatMathUtils.near(this.x, x, epsilon) && FloatMathUtils.near(this.y, y, epsilon); }

    public boolean near(float x, float y) { return FloatMathUtils.near(this.x, x) && FloatMathUtils.near(this.y, y); }

    public boolean equals(float x, float y) { return this.x == x && this.y == y; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point2F && equals((Point2F) obj);
    }

    public boolean equals(Point2F pt) {
        return equals(pt.x, pt.y);
    }

    @Override
    public String toString() {
        return "[" + x + ";" + y + "]";
    }

}
