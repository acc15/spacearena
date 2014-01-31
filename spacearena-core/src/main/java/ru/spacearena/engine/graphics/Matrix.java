package ru.spacearena.engine.graphics;

import ru.spacearena.engine.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public interface Matrix {

    <T> T getNativeMatrix(Class<T> clazz);

    Matrix translate(Point2F pt);
    Matrix rotate(float angle);
    Matrix scale(Point2F pt);

}
