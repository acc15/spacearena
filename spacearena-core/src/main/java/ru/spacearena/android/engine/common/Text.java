package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Text extends EngineObject {

    private String text = "";
    private int color = Color.WHITE;
    private float x = 0f, y = 0f;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.drawText(text, x, y);
    }
}
