package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private final Color color;
    private final Rect2FPP bounds = new Rect2FPP();

    public Rectangle() {
        this.color = Color.WHITE;
    }

    public Rectangle(BoundingBox2F aabb, Color color) {
        this.bounds.set(aabb);
        this.color = color;
    }

//    public Rectangle(float left, float top, float right, float bottom, Color color) {
//        this(left, top, right, bottom, Color.WHITE);
//    }
//
//    public Rectangle(float left, float top, float right, float bottom, int color) {
//        this.bounds.set(left, top, right, bottom);
//        this.color = color;
//    }

    @Override
    public void onDraw(DrawContext context) {
        //context.fill TODO
//        context.setColor(color);
//        context.fillRect(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
    }
}
