package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.spacearena.engine.handlers.DrawHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class BackgroundHandler implements DrawHandler {
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }

    public void onPreDraw(Canvas canvas) {
    }

    public void onPostDraw(Canvas canvas) {
    }
}
