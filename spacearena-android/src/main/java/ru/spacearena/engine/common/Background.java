package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.spacearena.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }
}
