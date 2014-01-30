package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Rect;
import ru.spacearena.android.engine.events.MotionType;

import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class EngineObject {

    public static final int ID_NULL = -1;

    private Engine engine = null;

    public int getId() {
        return ID_NULL;
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

    public void resize(Rect oldRect) {
    }

    public boolean touch(MotionType type, List<Point> points) {
        return false;
    }

    public void render(Canvas canvas) {
    }
}
