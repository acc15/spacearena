package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.primitives.ProjectionF;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public interface Shape2F {

    boolean obtainAxis(int n, boolean reference, Shape2F shape, Point2F axis);
    void calculateProjection(Point2F axis, ProjectionF projection);
    ShapeType getType();

}
