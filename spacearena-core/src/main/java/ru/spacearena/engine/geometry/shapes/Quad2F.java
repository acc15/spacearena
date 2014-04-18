package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class Quad2F extends AbstractPolyShape2F {

    public final Point2F p1 = new Point2F(), p2 = new Point2F(), p3 = new Point2F(), p4 = new Point2F();

    public Quad2F() {
    }

    public Quad2F(Rect2F rect) {
        setRect(rect);
    }

    public Quad2F(Quad2F quad) {
        setQuad(quad);
    }

    public Quad2F(float l, float t, float r, float b) {
        setRect(l,t,r,b);
    }

    public void setRect(Rect2F rect) {
        setRect(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
    }

    public void setRect(float l, float t, float r, float b) {
        p1.set(l,t);
        p2.set(l,b);
        p3.set(r,b);
        p4.set(r,t);
    }

    public void setQuad(Quad2F quad) {
        p1.set(quad.p1);
        p2.set(quad.p2);
        p3.set(quad.p3);
        p4.set(quad.p4);
    }

    public void transform(Matrix matrix) {
        matrix.transformPoint(p1);
        matrix.transformPoint(p2);
        matrix.transformPoint(p3);
        matrix.transformPoint(p4);
    }

    private static float min(float a, float b, float c, float d) {
        return FloatMathUtils.min(FloatMathUtils.min(FloatMathUtils.min(a, b), c), d);
    }

    private static float max(float a, float b, float c, float d) {
        return FloatMathUtils.max(FloatMathUtils.max(FloatMathUtils.max(a, b), c), d);
    }

    public float getMinX() {
        return min(p1.x, p2.x, p3.x, p4.x);
    }

    public float getMaxX() {
        return max(p1.x, p2.x, p3.x, p4.x);
    }

    public float getMinY() {
        return min(p1.y, p2.y, p3.y, p4.y);
    }

    public float getMaxY() {
        return max(p1.y, p2.y, p3.y, p4.y);
    }

    public float getCenterX() {
        return (p1.x + p2.x + p3.x + p4.x) / 4;
    }

    public float getCenterY() {
        return (p1.y + p2.y + p3.y + p4.y) / 4;
    }

    @Override
    public Point2F getPoint(int i) {
        switch (i) {
            case 0: return p1;
            case 1: return p2;
            case 2: return p3;
            case 3: return p4;
            default:throw new IllegalArgumentException("Illegal quad point index: " + i);
        }
    }

    @Override
    public Point2F getPoint(int i, Point2F pt) {
        return pt.set(getPoint(i));
    }

    public float getPointX(int i) {
        return getPoint(i).x;
    }

    public float getPointY(int i) {
        return getPoint(i).y;
    }

    public int getPointCount() {
        return 4;
    }

    public ShapeType getType() {
        return ShapeType.QUAD;
    }
}
