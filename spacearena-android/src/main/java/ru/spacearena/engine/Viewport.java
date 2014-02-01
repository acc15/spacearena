package ru.spacearena.engine;

import android.graphics.*;
import ru.spacearena.engine.input.MotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-29-01
 */
public class Viewport extends EngineContainer {

    private Matrix transform = new Matrix();
    private Point2F scale = Point2F.ONE;
    private Point2F position = Point2F.ZERO;

    public Viewport scale(Point2F sz) {
        this.scale = sz;
        return this;
    }

    public Viewport position(Point2F position) {
        this.position = position;
        return this;
    }

    public RectF getViewRect() {
        final Point2F viewSize = getViewSize();
        final Point2F halfSize = viewSize.div(2);
        final Point2F leftTop = position.sub(halfSize);
        final Point2F rightBottom = leftTop.add(viewSize);
        return new RectF(leftTop.getX(), leftTop.getY(), rightBottom.getX(), rightBottom.getY());
    }

    public Point2F getViewSize() {
        return getEngine().getDisplaySize().div(scale);
    }

    public Point2F mapPoint(Point2F screenPoint) {
        return screenPoint.div(scale).add(position.sub(getViewSize().div(2)));
    }

    @Override
    public boolean touch(MotionType type, List<Point2F> points) {
        final List<Point2F> remappedPoints = new ArrayList<Point2F>();
        for (final Point2F pt: points) {
            remappedPoints.add(mapPoint(pt));
        }
        return super.touch(type, remappedPoints);
    }

    @Override
    public void render(Canvas canvas) {
        final Matrix oldMatrix = canvas.getMatrix();

        final Point2F translate = mapPoint(Point2F.ZERO).negate();
        transform.setScale(scale.getX(), scale.getY());
        transform.preTranslate(translate.getX(), translate.getY());
        canvas.setMatrix(transform);
        try {
            super.render(canvas);
        } finally {
            canvas.setMatrix(oldMatrix);
        }
    }

}
