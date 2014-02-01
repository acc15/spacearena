package ru.spacearena.engine;

import android.graphics.Canvas;
import ru.spacearena.engine.input.MotionType;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;

    private EngineObject root;
    private Point2F displaySize;

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
        engineObject.attach(this);
    }

    public void init() {
        root.init();
    }

    public void process() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime >= 0) {
            final long delta = currentTime - lastTime;
            final float timeDelta = (float)delta/1000;
            root.process(timeDelta);
        }
        lastTime = currentTime;
        root.postProcess();
    }

    public void resize(Point2F newSize) {
        if (newSize.equals(displaySize)) {
            return;
        }
        final Point2F oldSize = this.displaySize;
        this.displaySize = newSize;
        root.resize(oldSize);
    }

    public void render(Canvas canvas) {
        root.render(canvas);
    }

    public boolean touch(MotionType type, List<Point2F> points) {
        return root.touch(type, points);
    }

    public Point2F getDisplaySize() {
        return displaySize;
    }

}
