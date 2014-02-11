package ru.spacearena.engine;

import ru.spacearena.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Point2F {

    public static final Point2F ZERO = new Point2F(0,0);
    public static final Point2F ONE = new Point2F(1,1);

    private final float x, y;

    private Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2F add(Point2F pt) {
        return cartesian(x + pt.x, y + pt.y);
    }

    public Point2F sub(Point2F pt) {
        return cartesian(x - pt.x, y - pt.y);
    }

    public Point2F mul(Point2F pt) {
        return cartesian(x * pt.x, y * pt.y);
    }

    public Point2F div(Point2F pt) {
        return cartesian(x / pt.x, y / pt.y);
    }

    public Point2F add(float v) {
        return cartesian(x + v, y + v);
    }

    public Point2F sub(float v) {
        return cartesian(x - v, y - v);
    }

    public Point2F mul(float v) {
        return cartesian(x * v, y * v);
    }

    public Point2F div(float v) {
        return cartesian(x / v, y / v);
    }

    public Point2F negate() {
        return cartesian(-x, -y);
    }

    public float cross(Point2F pt) {
        return x * pt.y - pt.x * y;
    }

    public float dot(Point2F pt) {
        return x * pt.x + y * pt.y;
    }

    public float magnitudeSquare() {
        return x * x + y * y;
    }

    public float magnitude() {
        return FloatMathUtils.sqrt(magnitudeSquare());
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
        return FloatMathUtils.toDegrees(FloatMathUtils.atan2(-x, y))+180;
    }

    public float angle(Point2F pt) {
        return (float)Math.atan2(x*pt.y - pt.x*y, dot(pt));
    }

    public Point2F resize(float newMagnitude) {
        return mul(newMagnitude / magnitude());
    }

    public float cosineOfAngle(Point2F pt) {
        return dot(pt) / (magnitude() * pt.magnitude());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean is(float v) {
        return FloatMathUtils.isEqual(x, v) && FloatMathUtils.isEqual(y, v);
    }

    public boolean isZero() {
        return is(0f);
    }

    public boolean isOne() {
        return is(1f);
    }

    public static Point2F polar(float angle, float distance) {
        final float rads = FloatMathUtils.toRadians(angle - 90);
        return cartesian(FloatMathUtils.cos(rads) * distance, FloatMathUtils.sin(rads) * distance);
    }

    public static Point2F cartesian(float x, float y) {
        return new Point2F(x, y);
    }

    public float min() {
        return Math.min(x,y);
    }

    public float max() {
        return Math.max(x,y);
    }

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
        return Point2F.cartesian(pts[offset], pts[offset+1]);
    }

    public static Point2F[] toPointArray(float... pts) {
        final Point2F[] points = new Point2F[pts.length/2];
        for (int i=0; i<points.length; i++) {
            points[i] = toPoint(pts, i*2);
        }
        return points;
    }

    @Override
    public String toString() {
        return "[" + x + ";" + y + "]";
    }

}
