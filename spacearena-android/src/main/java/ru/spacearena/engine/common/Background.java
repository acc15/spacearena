package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.RenderContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    public void render(RenderContext canvas) {
        canvas.fillScreen();
    }
}
