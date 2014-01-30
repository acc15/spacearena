package ru.spacearena.engine;

import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Point2F;
import ru.spacearena.engine.primitives.RectI;

import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class EngineObject {

    public static final int ID_NULL = -1;

    private Engine engine = null;
    private int id = ID_NULL;

    public int getId() {
        return id;
    }

    public EngineObject withId(int id) {
        this.id = id;
        return this;
    }

    public boolean attached() {
        return engine != null;
    }

    public void attach(Engine engine) {
        if (this.engine != null) {
            throw new IllegalStateException("Engine already attached");
        }
        this.engine = engine;

        final int id = getId();
        if (id != ID_NULL) {
            engine.register(id, this);
        }
    }

    public void detach() {
        checkEngine();
        final int id = getId();
        if (id != ID_NULL) {
            engine.unregister(id);
        }
        this.engine = null;
    }

    private void checkEngine() {
        if (engine == null) {
            throw new IllegalStateException("Engine not attached");
        }
    }

    public Engine getEngine() {
        checkEngine();
        return engine;
    }

    public Engine getEngineOrNull() {
        return engine;
    }

    public void init() {
    }

    public boolean process(float time) {

        return true;
    }

    public void resize(RectI oldRect) {
    }

    public boolean touch(MotionType type, List<Point2F> points) {
        return false;
    }

    public void render(RenderContext context) {
    }
}
