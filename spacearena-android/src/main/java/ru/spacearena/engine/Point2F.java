package ru.spacearena.engine;

import android.util.FloatMath;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Point2F {

    public static final Point2F ZERO = new Point2F(0,0);
    public static final Point2F ONE = new Point2F(1,1);

    private float x, y;

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

    public float scalarProduct(Point2F pt) {
        return x * pt.x + y * pt.y;
    }

    public float magnitudeSquare() {
        return x * x + y * y;
    }

    public float magnitude() {
        return FloatMath.sqrt(magnitudeSquare());
    }

    public Point2F resize(float newMagnitude) {
        return mul(newMagnitude / magnitude());
    }

    public float cosineOfAngle(Point2F pt) {
        return scalarProduct(pt) / (magnitude() * pt.magnitude());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public static Point2F polar(float angle, float distance) {
        final float rads = (float)Math.toRadians(angle-90);
        return cartesian((float) Math.cos(rads) * distance, (float) Math.sin(rads) * distance);
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

    @Override
    public String toString() {
        return "[" + x + ";" + y + "]";
    }
}
