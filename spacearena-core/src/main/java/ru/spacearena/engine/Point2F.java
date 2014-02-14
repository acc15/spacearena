package ru.spacearena.engine;

import ru.spacearena.engine.util.FloatMathUtils;

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
        return xy(x + pt.x, y + pt.y);
    }

    public Point2F sub(Point2F pt) {
        return xy(x - pt.x, y - pt.y);
    }

    public Point2F mul(Point2F pt) {
        return xy(x * pt.x, y * pt.y);
    }

    public Point2F div(Point2F pt) {
        return xy(x / pt.x, y / pt.y);
    }

    public Point2F add(float v) {
        return xy(x + v, y + v);
    }

    public Point2F sub(float v) {
        return xy(x - v, y - v);
    }

    public Point2F mul(float v) {
        return xy(x * v, y * v);
    }

    public Point2F div(float v) {
        return xy(x / v, y / v);
    }

    public Point2F negate() {
        return xy(-x, -y);
    }

    public float cross(Point2F pt) {
        return FloatMathUtils.cross(x, y, pt.x, pt.y);
    }

    public float dot(Point2F pt) {
        return FloatMathUtils.dot(x, y, pt.x, pt.y);
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
        return FloatMathUtils.angle(x, y);
    }

    public float angle(Point2F pt) {
        return FloatMathUtils.angle(x, y, pt.x, pt.y);
    }

    public Point2F resize(float newMagnitude) {
        return mul(newMagnitude / length());
    }

    public float cosineOfAngle(Point2F pt) {
        return dot(pt) / (length() * pt.length());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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

    public static Point2F polar(float angle, float distance) {
        final float rads = FloatMathUtils.toRadians(angle - 90);
        return xy(FloatMathUtils.cos(rads) * distance, FloatMathUtils.sin(rads) * distance);
    }

    public static Point2F xy(float x, float y) {
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
        return Point2F.xy(pts[offset], pts[offset + 1]);
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
