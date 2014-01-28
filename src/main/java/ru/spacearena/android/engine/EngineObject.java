package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public interface EngineObject {
    boolean process(float timeDelta);
    boolean onTouch(MotionEvent motionEvent);
    void render(Canvas canvas);
}
