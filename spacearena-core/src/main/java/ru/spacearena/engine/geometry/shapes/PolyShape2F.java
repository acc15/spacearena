package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Line2F;
import ru.spacearena.engine.geometry.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public interface PolyShape2F {

    void getPoints(float[] points, int start, int pointCount);

    Line2F getEdge(int i);
    Line2F getEdge(int i, Line2F edge);
    Point2F getPoint(int i);
    Point2F getPoint(int i, Point2F pt);

    float getPointX(int i);
    float getPointY(int i);
    int getPointCount();

}
