package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.geometry.shapes.Rect2ID;

import java.io.Serializable;

/**
* @author Vyacheslav Mayorov
* @since 2014-07-04
*/
public final class CharData implements Serializable {
    private final Rect2ID rect = new Rect2ID();
    private int offsetY, offsetX;
    private int advance;
    private char character;

    public Rect2ID getRect() {
        return rect;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getX() {
        return rect.x;
    }

    public int getY() {
        return rect.y;
    }

    public void setX(int x) {
        rect.x = x;
    }

    public void setY(int y) {
        rect.y = y;
    }

    public int getWidth() {
        return rect.w;
    }

    public int getHeight() {
        return rect.h;
    }

    public void setWidth(int width) {
        rect.w = width;
    }

    public void setHeight(int height) {
        rect.h = height;
    }
}
