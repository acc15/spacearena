package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.geometry.shapes.PackedRect2I;

import java.io.Serializable;

/**
* @author Vyacheslav Mayorov
* @since 2014-07-04
*/
public final class CharData implements PackedRect2I, Serializable {
    private int x,y,w,h;
    private int offsetY, offsetX;
    private int advance;
    private char character;

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
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimension(int width, int height) {
        this.w = width;
        this.h = height;
    }
}
