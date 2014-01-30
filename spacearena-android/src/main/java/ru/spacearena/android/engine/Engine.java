package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Rect;
import ru.spacearena.android.engine.events.MotionType;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static int nextId() {
        return COUNTER.getAndIncrement();
    }

    private long lastTime = -1;

    private EngineObject root;
    private Rect displayRect;
    private HashMap<Integer, EngineObject> objects = new HashMap<Integer, EngineObject>();

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
        engineObject.attach(this);
    }

    public void register(int id, EngineObject object) {
        if (objects.containsKey(id)) {
            throw new IllegalArgumentException("EngineObject with id " + id + " already registered");
        }
        this.objects.put(id, object);
    }

    public void unregister(int id) {
        if (!objects.containsKey(id)) {
            throw notRegistered(id);
        }
        this.objects.remove(id);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int id) {
        final T val = (T)objects.get(id);
        if (val == null) {
            throw notRegistered(id);
        }
        return val;
    }

    private IllegalArgumentException notRegistered(int id) {
        return new IllegalArgumentException("EngineObject with id " + id + " isn't registered");
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
    }

    public void resize(Rect newRect) {
        if (newRect.equals(displayRect)) {
            return;
        }
        final Rect oldRect = this.displayRect;
        this.displayRect = newRect;
        root.resize(oldRect);
    }

    public void render(Canvas canvas) {
        root.render(canvas);
    }

    public boolean touch(MotionType type, List<Point> points) {
        return root.touch(type, points);
    }

    public Rect getDisplayRect() {
        return displayRect;
    }
}
