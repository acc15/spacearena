package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Line2F;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.primitives.ProjectionF;

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

    private void preparePath(DrawContext drawContext) {

        final float x0 = getPointX(0), y0 = getPointY(0);

        final Path p = drawContext.preparePath();
        p.moveTo(x0, y0);

        final int pc = getPointCount();
        for (int i=1; i<pc; i++) {
            p.lineTo(getPointX(i), getPointY(i));
        }
        p.lineTo(x0, y0);
    }

    public void stroke(DrawContext drawContext) {
        preparePath(drawContext);
        drawContext.drawPath();
    }

    public void fill(DrawContext drawContext) {
        preparePath(drawContext);
        drawContext.fillPath();
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
