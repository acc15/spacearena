package ru.spacearena.engine;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.SparseArray;
import ru.spacearena.engine.input.MotionType;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;

    private EngineObject root;
    private Rect displayRect;
    private SparseArray<EngineObject> objects = new SparseArray<EngineObject>();

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
        engineObject.attach(this);
    }

    public void register(int id, EngineObject object) {
        if (objects.get(id) != null) {
            throw new IllegalArgumentException("EngineObject with id " + id + " already registered");
        }
        this.objects.put(id, object);
    }

    public void unregister(int id) {
        if (objects.get(id) == null) {
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

    public boolean touch(MotionType type, List<PointF> points) {
        return root.touch(type, points);
    }

    public Rect getDisplayRect() {
        return displayRect;
    }

}
