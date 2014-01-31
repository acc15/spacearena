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
    private PointF scale = new PointF(1,1);
    private PointF position = new PointF(0,0);

    public Viewport scale(PointF sz) {
        this.scale.set(sz);
        return this;
    }

    public Viewport position(PointF translate) {
        this.position.set(translate);
        return this;
    }

    private RectF getViewRectFor(PointF pt) {
        final Rect displayRect = getEngine().getDisplayRect();
        return new RectF(pt.x, pt.y, displayRect.width() * scale.x, displayRect.height() * scale.y);
    }

    public RectF getViewRect() {
        return getViewRectFor(position);
    }

    public PointF mapPoint(PointF screenPoint) {
        final Rect displayRect = getEngine().getDisplayRect();
        final float displayWidth = displayRect.width() / scale.x;
        final float displayHeight = displayRect.height() / scale.y;
        final float left = position.x - displayWidth / 2;
        final float top = position.y - displayHeight / 2;
        return new PointF(screenPoint.x / scale.x + left, screenPoint.y / scale.y + top);
    }

    @Override
    public boolean touch(MotionType type, List<PointF> points) {
        final List<PointF> remappedPoints = new ArrayList<PointF>();
        for (final PointF pt: points) {
            remappedPoints.add(mapPoint(pt));
        }
        return super.touch(type, remappedPoints);
    }

    @Override
    public void render(Canvas canvas) {
        final Matrix oldMatrix = canvas.getMatrix();

        final float x = position.x - canvas.getWidth()/(scale.x*2);
        final float y = position.y - canvas.getHeight()/(scale.y*2);

        final PointF leftTop = mapPoint(new PointF(0,0));
        transform.setScale(scale.x, scale.y);
        transform.preTranslate(-leftTop.x, -leftTop.y);
        canvas.setMatrix(transform);
        try {
            super.render(canvas);
        } finally {
            canvas.setMatrix(oldMatrix);
        }
    }

}
