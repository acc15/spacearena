package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class Rectangle extends EngineObject {

    private int color = Color.WHITE;
    private float left, top, right, bottom;

    public Rectangle() {
    }

    public Rectangle(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rectangle(int color, float left, float top, float right, float bottom) {
        this.color = color;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void offset(float dx, float dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        context.fillRect(left, top, right, bottom);
    }
}
