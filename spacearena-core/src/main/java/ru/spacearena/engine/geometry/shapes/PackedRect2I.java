package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-22-04
 */
public interface PackedRect2I {

    int getWidth();
    int getHeight();
    int getX();
    int getY();
    void setPosition(int x, int y);

}
