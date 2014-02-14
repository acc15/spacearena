package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.BLACK);
        context.fill();
    }

}
