package ru.spacearena.engine;

import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Matrix2F;
import ru.spacearena.engine.primitives.Point2F;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-29-01
 */
public class Viewport extends EngineContainer {

    private final Matrix2F concat = new Matrix2F();
    private Point2F scale = new Point2F(1,1);

    public Viewport scale(Point2F sz) {
        this.scale = sz;
        concat.scale(sz.x, sz.y);
        return this;
    }


    @Override
    public boolean touch(MotionType type, final List<Point2F> points) {
        final List<Point2F> remappedPoints = new ArrayList<Point2F>();
        for (final Point2F pt: points) {
            remappedPoints.add(new Point2F(pt.x / scale.x, pt.y / scale.y));
        }
        return super.touch(type, remappedPoints);
    }

    @Override
    public void render(RenderContext context) {
        final Matrix2F oldMatrix = context.getMatrix();
        context.setMatrix(concat);
        try {
            super.render(context);
        } finally {
            context.setMatrix(oldMatrix);
        }
    }
}
