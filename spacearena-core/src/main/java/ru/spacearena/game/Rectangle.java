package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private int color = Color.WHITE;
    private AABB bounds = new AABB();

    public Rectangle() {
    }

    public Rectangle(float left, float top, float right, float bottom) {
        this.bounds.set(left, top, right, bottom);
    }

    public Rectangle(int color, float left, float top, float right, float bottom) {
        this(left, top, right, bottom);
        this.color = color;
    }

    public AABB getAABB() {
        return bounds;
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.fillRect(bounds.minX, bounds.minY, bounds.maxX, bounds.maxY);
    }
}
