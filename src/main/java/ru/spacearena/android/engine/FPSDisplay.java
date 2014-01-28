package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class FPSDisplay implements EngineObject {
    private final Paint paint = new Paint();
    private float fps = 0;

    public FPSDisplay() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
    }

    public boolean process(float timeDelta) {
        fps = 1/timeDelta;
        return true;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        return false;
    }

    public void render(Canvas canvas) {
        canvas.drawText("FPS: " + fps, 0, 30, paint);
    }
}
