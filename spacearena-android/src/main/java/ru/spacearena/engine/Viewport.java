package ru.spacearena.engine;

import android.graphics.RectF;
import ru.spacearena.engine.common.TransformHandler;
import ru.spacearena.engine.handlers.SizeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-29-01
 */
public class Viewport extends NewEngineObject {

    private TransformHandler transformHandler = new TransformHandler();

    private Point2F displaySize;
    private RectF viewRect;

    public Viewport() {
        addDrawHandler(transformHandler);
        addSizeHandler(new SizeHandler() {
            public void onSize(Point2F newSize) {
                displaySize = newSize;
            }
        });
    }

    public Viewport setZoom(Point2F sz) {
        transformHandler.setScale(sz);
        return this;
    }

    public Viewport setPosition(Point2F position) {
        transformHandler.setTranslate(position.add(displaySize.div(2)).negate());
        return this;
    }

    public RectF getViewRect() {
        final Point2F viewSize = getViewSize();
        final Point2F lt = transformHandler.getTranslate().negate().sub(viewSize.div(2));
        final Point2F rb = lt.add(viewSize);
        viewRect.set(lt.getX(), lt.getY(), rb.getX(), rb.getY());
        return viewRect;
    }

    public Point2F getViewSize() {
        return displaySize.div(transformHandler.getScale());
    }

    public Point2F mapPoint(Point2F screenPoint) {
        return screenPoint.div(transformHandler.getScale()).add(transformHandler.getTranslate().negate().sub(displaySize.div(2)));
    }

    @Override
    public boolean onTouch(Collection<Point2F> points) {
        final List<Point2F> remappedPoints = new ArrayList<Point2F>();
        for (final Point2F pt: points) {
            remappedPoints.add(mapPoint(pt));
        }
        return super.onTouch(remappedPoints);
    }

}
