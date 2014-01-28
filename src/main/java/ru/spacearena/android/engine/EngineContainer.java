package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

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
    public void init(Dimension dimension) {
        for (EngineObject obj: objects) {
            obj.init(dimension);
        }
    }

    public boolean process(Frame frame) {
        final Iterator<EngineObject> iter = objects.iterator();
        while (iter.hasNext()) {
            final EngineObject obj = iter.next();
            if (!obj.process(frame)) {
                iter.remove();
            }
        }
        return true;
    }

    public void onSize(Dimension dimension) {
        for (EngineObject obj: objects) {
            obj.onSize(dimension);
        }
    }

    public boolean onTouch(MotionEvent motionEvent) {
        for (EngineObject obj: objects) {
            if (obj.onTouch(motionEvent)) {
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
        return this;
    }
}
