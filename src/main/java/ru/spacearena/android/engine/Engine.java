package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;

    private EngineObject root;

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
    }

    public void process() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime >= 0) {
            final long delta = currentTime - lastTime;
            final float secDelta = (float)delta/1000;
            root.process(secDelta);
        }
        lastTime = currentTime;
    }

    public void render(Canvas canvas) {
        root.render(canvas);
    }

    public boolean onTouch(MotionEvent event) {
        return root.onTouch(event);
    }
}
