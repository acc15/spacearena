package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Line2F;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.primitives.ProjectionF;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.BufUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public abstract class AbstractPolyShape2F implements PolyShape2F, Shape2F {

    public void getPoints(float[] points, int start, int pointCount) {
        for (int i=0; i<pointCount; i++) {
            points[(i+start)*2] = getPointX(i);
            points[(i+start)*2+1] = getPointY(i);
        }
    }

    public Line2F getEdge(int i, Line2F edge) {
        edge.x1 = getPointX(i);
        edge.y1 = getPointY(i);
        edge.x2 = getPointX(i + 1);
        edge.y2 = getPointY(i + 1);
        return edge;
    }

    public Line2F getEdge(int i) {
        return getEdge(i, new Line2F());
    }

    public Point2F getPoint(int i) {
        return getPoint(i, new Point2F());
    }

    public Point2F getPoint(int i, Point2F pt) {
        pt.x = getPointX(i);
        pt.y = getPointY(i);
        return pt;
    }

    public void stroke(DrawContext drawContext) {
        final int pointCount = getPointCount();
        getPoints(BufUtils.POINT_BUF, 0, pointCount);
        drawContext.drawPoly(BufUtils.POINT_BUF, 0, pointCount);
    }

    public void fill(DrawContext drawContext) {
        final int pointCount = getPointCount();
        getPoints(BufUtils.POINT_BUF, 0, pointCount);
        drawContext.fillPoly(BufUtils.POINT_BUF, 0, pointCount);
    }

    public void calculateProjection(Point2F axis, ProjectionF projection) {
        for (int i=0; i<getPointCount(); i++) {
            final float x = getPointX(i), y = getPointY(i);
            projection.accumulate(axis.dot(x,y));
        }
    }

    public boolean obtainAxis(int n, boolean references, Shape2F shape, Point2F axis) {
        final int pointCount = getPointCount();
        if (n >= pointCount) {
            return false;
        }

        final float x1 = getPointX(n), y1 = getPointY(n),
                    x2 = getPointX(n+1), y2 = getPointY(n+1);
        axis.set(x2 - x1, y2 - y1).rperp().normalize();
        return true;
    }
}
