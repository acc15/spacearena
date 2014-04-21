package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public abstract class AbstractRect2I implements Rect2I {

    public int getX() {
        return getLeft();
    }

    public int getY() {
        return getTop();
    }

    public boolean contains(Rect2I r) {
        return getLeft() <= r.getLeft() &&
               getRight() >= r.getRight() &&
               getTop() <= r.getTop() &&
               getBottom() >= r.getBottom();
    }
}
