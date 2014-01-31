package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.RenderContext;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class FPSDisplay extends EngineObject {
    private float fps = 0;

    public boolean process(float time) {
        fps = 1f/time;
        return true;
    }

    public void render(RenderContext context) {
        context.setColor(Color.WHITE);
        context.setTextSize(25);
        context.drawText("FPS: " + fps, 0, 30);
    }
}
