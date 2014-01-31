package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ru.spacearena.engine.EngineObject;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class FPSDisplay extends EngineObject {
    private float fps = 0;

    private final Paint paint = new Paint();

    public FPSDisplay() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
    }

    public boolean process(float time) {
        fps = 1f/time;
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawText("FPS: " + fps, 0, 30, paint);
    }
}
