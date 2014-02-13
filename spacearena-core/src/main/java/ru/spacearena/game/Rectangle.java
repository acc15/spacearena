package ru.spacearena.game;

import ru.spacearena.android.engine.common.PhysicalObject;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.graphics.DrawContext;
import ru.spacearena.android.engine.input.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends PhysicalObject {

    private int color = Color.WHITE;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean onInput(InputEvent inputEvent) {

        return true;
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(color);
        context.fillRect(0, 0, 1, 1);
    }
}
