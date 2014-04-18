package ru.spacearena.engine.common.viewport;

import ru.spacearena.engine.geometry.shapes.Quad2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;

/**
* @author Vyacheslav Mayorov
* @since 2014-18-04
*/
public interface ViewportArea {
    Quad2F getQuad();
    Rect2FP getRect();
}
