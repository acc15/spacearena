package ru.spacearena.engine.primitives;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Point2F {

    public float x, y;

    public Point2F() {
        this(0f, 0f);
    }

    public Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2F copy() {
        return new Point2F(x, y);
    }

    public Point2F add(Point2F pt) {
        x += pt.x;
        y += pt.y;
        return this;
    }

    public Point2F sub(Point2F pt) {
        x -= pt.x;
        y -= pt.y;
        return this;
    }

    public Point2F mul(Point2F pt) {
        x *= pt.x;
        y *= pt.y;
        return this;
    }

    public Point2F div(Point2F pt) {
        x /= pt.x;
        y /= pt.y;
        return this;
    }

    public Point2F add(float v) {
        x += v;
        y += v;
        return this;
    }

    public Point2F sub(float v) {
        x -= v;
        y -= v;
        return this;
    }

    public Point2F mul(float v) {
        x *= v;
        y *= v;
        return this;
    }

    public Point2F div(float v) {
        x /= v;
        y /= v;
        return this;
    }

    public Point2F length(float newLength) {
        return mul(newLength/length());
    }

    public float lengthSquare() {
        return x*x + y*y;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquare());
    }

    public Point2F identity() {
        return div(length());
    }

    public float cosineOfAngle(Point2F pt) {
        return (x * pt.x + y * pt.y) / (length() * pt.length());
    }

}
