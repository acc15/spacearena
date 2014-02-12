package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.graphics.DrawContext;

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
