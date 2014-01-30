package ru.spacearena.engine;

import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Point2F;
import ru.spacearena.engine.primitives.RectI;

import java.util.HashMap;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;

    private EngineObject root;
    private RectI displayRect;
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

    public void resize(RectI newRect) {
        if (newRect.equals(displayRect)) {
            return;
        }
        final RectI oldRect = this.displayRect;
        this.displayRect = newRect;
        root.resize(oldRect);
    }

    public void render(RenderContext context) {
        root.render(context);
    }

    public boolean touch(MotionType type, List<Point2F> points) {
        return root.touch(type, points);
    }

    public RectI getDisplayRect() {
        return displayRect;
    }
}
