package ru.spacearena.engine;

import android.graphics.Canvas;

import java.util.Collection;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class EngineObject {

    public static final int ID_NULL = -1;

    private Engine engine = null;

    public boolean attached() {
        return engine != null;
    }

    public void attach(Engine engine) {
        if (this.engine != null) {
            throw new IllegalStateException("Engine already attached");
        }
        this.engine = engine;
    }

    public void detach() {
        checkEngine();
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

    public void postProcess() {
    }

    public void resize(Point2F oldSize) {
    }

    public boolean touch(Collection<Point2F> points) {
        return false;
    }

    public void render(Canvas canvas) {
    }
}
