package ru.spacearena.engine.common.viewport;

/**
* @author Vyacheslav Mayorov
* @since 2014-18-04
*/
public interface ViewportAdjustStrategy {
    void adjustViewport(float width, float height, Viewport tx);
}
