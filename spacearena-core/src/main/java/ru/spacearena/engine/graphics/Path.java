package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-03
 */
public interface Path {

    void moveTo(float x, float y);
    void lineTo(float x, float y);
    void quadTo(float x1, float y1, float x2, float y2);

}
