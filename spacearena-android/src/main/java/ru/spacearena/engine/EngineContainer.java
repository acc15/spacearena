package ru.spacearena.engine;

import android.graphics.Canvas;
import ru.spacearena.engine.input.MotionType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class EngineContainer extends EngineObject {

    private List<EngineObject> objects = new ArrayList<EngineObject>();

    @Override
    public void init() {
        for (EngineObject obj: objects) {
            obj.init();
        }
    }

    @Override
    public void attach(Engine engine) {
        super.attach(engine);
        for (EngineObject obj: objects) {
            obj.attach(engine);
        }
    }

    @Override
    public void detach() {
        for (EngineObject obj: objects) {
            obj.detach();
        }
        super.detach();
    }

    public boolean process(float time) {
        final Iterator<EngineObject> iter = objects.iterator();
        while (iter.hasNext()) {
            final EngineObject obj = iter.next();
            if (!obj.process(time)) {
                obj.detach();
                iter.remove();
            }
        }
        return true;
    }

    @Override
    public void postProcess() {
        for (EngineObject obj: objects) {
            obj.postProcess();
        }
    }

    public void resize(Point2F oldSize) {
        for (EngineObject obj: objects) {
            obj.resize(oldSize);
        }
    }

    public boolean touch(MotionType type, List<Point2F> points) {
        for (EngineObject obj: objects) {
            if (obj.touch(type, points)) {
                return true;
            }
        }
        return false;
    }

    public void render(Canvas canvas) {
        for (EngineObject obj: objects) {
            obj.render(canvas);
        }
    }

    public EngineContainer add(EngineObject object) {
        this.objects.add(object);
        final Engine engine = getEngineOrNull();
        if (engine != null) {
            object.attach(engine);
        }
        return this;
    }
}
