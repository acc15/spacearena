package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Background extends EngineObject {
    private final int color;

    private Engine engine;

    public Background() {
        this(Color.BLACK);
    }

    public Background(int color) {
        this.color = color;
    }

    @Override
    public void onAttach(Engine engine) {
        this.engine = engine;
    }

    public void onDraw(DrawContext context) {
        context.fillColor(color);
        context.fillRect(0, 0, engine.getWidth(), engine.getHeight());
    }

}
