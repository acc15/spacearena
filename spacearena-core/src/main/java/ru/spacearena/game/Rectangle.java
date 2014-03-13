package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.vmsoftware.math.geometry.shapes.Rect2FPP;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private final int color;
    private final Rect2FPP bounds = new Rect2FPP();

    public Rectangle() {
        this.color = Color.WHITE;
    }

    public Rectangle(float left, float top, float right, float bottom) {
        this(Color.WHITE, left, top, right, bottom);
    }

    public Rectangle(int color, float left, float top, float right, float bottom) {
        this.color = color;
        this.bounds.set(left, top, right, bottom);
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.fillRect(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
    }
}
