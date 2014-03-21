package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.Path;

import java.awt.geom.Path2D;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-03
 */
public class Java2DPath implements Path {

    final Path2D.Float path;

    public Java2DPath() {
        this.path = new Path2D.Float();
    }

    public Java2DPath(Path2D.Float path) {
        this.path = path;
    }

    public void reset() {
        path.reset();
    }

    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    public void lineTo(float x, float y) {
        path.lineTo(x, y);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        path.quadTo(x1, y1, x2, y2);
    }
}
