package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.DrawContext;

import java.util.Collection;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private static EngineFactory factory = null;

    public static EngineFactory getFactory() {
        if (factory == null) {
            throw new IllegalStateException("Engine wasn't initialized. Call Engine.init() before use");
        }
        return factory;
    }

    public static synchronized void init(EngineFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Engine factory can't be null");
        }
        Engine.factory = factory;
    }

    private long lastTime = -1;
    private EngineObject root;

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
    }

    public boolean onUpdate() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime < 0) {
            lastTime = currentTime;
        }
        final float seconds = (float)(currentTime-lastTime)/1000;
        lastTime = currentTime;
        return root.onUpdate(seconds);
    }

    public void onSize(float width, float height) {
        root.onSize(width, height);
    }

    public void onDraw(DrawContext context) {
        EngineObject.drawObject(context, root);
    }

    public boolean onTouch(Collection<Point2F> points) {
        return root.onTouch(points);
    }

}
