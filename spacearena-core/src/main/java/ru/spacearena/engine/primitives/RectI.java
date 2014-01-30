package ru.spacearena.engine.primitives;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class RectI {

    public int left = 0, top = 0, right = 0, bottom = 0;

    public RectI() {
    }

    public RectI(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getHeight() {
        return bottom - top;
    }

    public int getWidth() {
        return right - left;
    }
}
