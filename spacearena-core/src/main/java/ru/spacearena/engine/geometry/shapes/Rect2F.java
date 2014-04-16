package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public interface Rect2F {
    float getMinX();
    float getMaxX();
    float getMinY();
    float getMaxY();
    float getCenterX();
    float getCenterY();
    float getWidth();
    float getHeight();
    float getHalfWidth();
    float getHalfHeight();
}
