package ru.spacearena.engine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import ru.spacearena.engine.input.MotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-29-01
 */
public class Viewport extends EngineContainer {

    private Matrix concat = new Matrix();
    private PointF scale = new PointF(1,1);

    public Viewport scale(PointF sz) {
        this.scale.set(sz);
        return this;
    }

    @Override
    public boolean touch(MotionType type, List<PointF> points) {
        final List<PointF> remappedPoints = new ArrayList<PointF>();
        for (final PointF pt: points) {
            remappedPoints.add(new PointF(pt.x / scale.x, pt.y / scale.y));
        }
        return super.touch(type, remappedPoints);
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
