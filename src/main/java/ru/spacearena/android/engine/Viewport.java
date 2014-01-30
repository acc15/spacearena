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
public class Viewport extends EngineObject {

    public static final int ID = Engine.nextId();

    private final Matrix translate = new Matrix();
    private final Matrix scale = new Matrix();
    private final Matrix concat = new Matrix();

    private EngineObject object;

    @Override
    public int getId() {
        return ID;
    }

    public void moveTo(Point pt) {
        translate.setTranslate(pt.getX(), pt.getY());
        concat.setConcat(translate, scale);
    }

    public void scaleTo(Point sz) {
        scale.setScale(sz.getX(), sz.getY());
        concat.setConcat(translate, scale);
    }

    @Override
    public boolean touch(MotionType type, final List<Point> points) {
        return object.touch(type, new AbstractList<Point>() {
            @Override
            public Point get(int i) {
                final Point pt = points.get(i);
                final float[] values = new float[] { pt.getX(), pt.getY() };
                concat.mapPoints(values);
                return Point.create(values);
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
            object.render(canvas);
        } finally {
            canvas.setMatrix(oldMatrix);
        }
    }
}
