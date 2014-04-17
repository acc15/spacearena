package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    private final Color color;

    public Background() {
        this(Color.BLACK);
    }

    public Background(Color color) {
        this.color = color;
    }

    public void onDraw(DrawContext context) {
        context.color(color).clear();
    }

}
