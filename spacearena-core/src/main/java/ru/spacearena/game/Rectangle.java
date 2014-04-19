package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext2f;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private final Color color;
    private final Rect2FP bounds = new Rect2FP();

    public Rectangle(float left, float top, float right, float bottom, Color color) {
        this.bounds.setBounds(left, top, right, bottom);
        this.color = color;
    }

    @Override
    public void onDraw(DrawContext2f context) {
        context.color(color).fillRect(bounds.p1.x, bounds.p1.y, bounds.p2.x, bounds.p2.y);
    }
}
