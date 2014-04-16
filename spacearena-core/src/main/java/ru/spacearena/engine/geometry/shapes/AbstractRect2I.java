package ru.spacearena.engine.geometry.shapes;

import java.io.Serializable;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public abstract class AbstractRect2I implements Rect2I, Serializable {

    public boolean contains(Rect2I r) {
        return getLeft() <= r.getLeft() &&
               getRight() >= r.getRight() &&
               getTop() <= r.getTop() &&
               getBottom() >= r.getBottom();
    }
}
