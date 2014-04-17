package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class Quad2F extends AbstractPolyShape2F {

    public final Point2F p1 = new Point2F(), p2 = new Point2F(), p3 = new Point2F(), p4 = new Point2F();

    public void set(Rect2F rect) {
        setBounds(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
    }

    public void setBounds(float l, float t, float r, float b) {
        p1.set(l,t);
        p2.set(l,b);
        p3.set(r,b);
        p4.set(r,t);
    }

    public void transform(Matrix matrix) {
        matrix.transformPoint(p1);
        matrix.transformPoint(p2);
        matrix.transformPoint(p3);
        matrix.transformPoint(p4);
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
