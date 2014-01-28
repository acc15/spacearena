package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background implements EngineObject {
    public boolean process(float timeDelta) {
        return true;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        return false;
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }
}
