package ru.spacearena.engine;

import android.graphics.Canvas;

import java.util.Collection;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;
    private NewEngineObject root;

    public Engine(NewEngineObject engineObject) {
        this.root = engineObject;
    }

    public void onUpdate() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime < 0) {
            lastTime = currentTime;
        }
        root.onUpdate((float)(currentTime-lastTime)/1000);
    }

    public void onSize(Point2F newSize) {
        root.onSize(newSize);
    }

    public void onDraw(Canvas canvas) {
        try {
            root.onPreDraw(canvas);
            root.onDraw(canvas);
        } finally {
            root.onPostDraw(canvas);
        }
    }

    public boolean onTouch(Collection<Point2F> points) {
        return root.onTouch(points);
    }

}
