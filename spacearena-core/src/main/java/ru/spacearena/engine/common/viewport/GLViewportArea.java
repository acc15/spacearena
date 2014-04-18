package ru.spacearena.engine.common.viewport;

import ru.spacearena.engine.geometry.shapes.Quad2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;

/**
* @author Vyacheslav Mayorov
* @since 2014-18-04
*/
public class GLViewportArea implements ViewportArea {
    private final Rect2FP r = new Rect2FP(-1,1,1,-1);
    private final Quad2F q = new Quad2F(r);

    public Quad2F getQuad() {
        return q;
    }

    public Rect2FP getRect() {
        return r;
    }

    public static GLViewportArea getInstance() {
        return instance;
    }

    private GLViewportArea() {
    }

    private static final GLViewportArea instance = new GLViewportArea();
}
