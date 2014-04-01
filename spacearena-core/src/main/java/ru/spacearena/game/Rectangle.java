package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.graphics.ColorU;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private final int color;
    private final Rect2FPP bounds = new Rect2FPP();

    public Rectangle() {
        this.color = ColorU.WHITE;
    }

    public Rectangle(BoundingBox2F aabb, int color) {
        this.bounds.set(aabb);
        this.color = color;
    }

    public Rectangle(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, ColorU.WHITE);
    }

    public Rectangle(float left, float top, float right, float bottom, int color) {
        this.bounds.set(left, top, right, bottom);
        this.color = color;
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.fillRect(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
    }
}
