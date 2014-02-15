package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    private final int color;

    public Background() {
        this(Color.BLACK);
    }

    public Background(int color) {
        this.color = color;
    }

    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(color);
        context.fill();
    }

}
