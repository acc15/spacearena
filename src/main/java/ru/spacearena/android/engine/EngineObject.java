package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class EngineObject {

    public void init(Dimension dimension) {
    }

    public boolean process(Frame frame) {
        return true;
    }

    public void onSize(Dimension dimension) {
    }

    public boolean onTouch(MotionEvent motionEvent) {
        return false;
    }

    public void render(Canvas canvas) {
    }
}
