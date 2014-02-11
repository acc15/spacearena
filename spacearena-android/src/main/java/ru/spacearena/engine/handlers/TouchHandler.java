package ru.spacearena.engine.handlers;

import ru.spacearena.engine.Point2F;

import java.util.Collection;

/**
* @author Vyacheslav Mayorov
* @since 2014-11-02
*/
public interface TouchHandler {
    boolean onTouch(Collection<Point2F> points);
}
