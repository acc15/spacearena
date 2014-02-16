package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.graphics.DrawContext;

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
    public void onInit(Engine engine) {
        this.engine = engine;
    }

    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.fillRect(0, 0, engine.getWidth(), engine.getHeight());
    }

}
