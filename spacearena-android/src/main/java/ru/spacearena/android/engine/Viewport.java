package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import ru.spacearena.android.engine.events.MotionType;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-29-01
 */
public class Viewport extends EngineContainer {

    private final Matrix concat = new Matrix();
    private Point scale = Point.zero(2);


    public Viewport scale(Point sz) {
        this.scale = sz;
        concat.setScale(sz.getX(), sz.getY());
        return this;
    }


    @Override
    public boolean touch(MotionType type, final List<Point> points) {
        return super.touch(type, new AbstractList<Point>() {
            @Override
            public Point get(int i) {
                final Point pt = points.get(i);
                return Point.create(pt.getX() / scale.getX(), pt.getY() / scale.getY());
            }

            @Override
            public int size() {
                return points.size();
            }
        });
    }

    @Override
    public void render(Canvas canvas) {
        final Matrix oldMatrix = canvas.getMatrix();
        canvas.setMatrix(concat);
        try {
            super.render(canvas);
        } finally {
            canvas.setMatrix(oldMatrix);
        }
    }
}
