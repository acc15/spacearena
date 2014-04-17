package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class Quad2F extends AbstractPolyShape2F {

    public final Point2F p1 = new Point2F(), p2 = new Point2F(), p3 = new Point2F(), p4 = new Point2F();

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
