package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class Rect2ID extends AbstractRect2I {

    public int x,y,w,h;

    public Rect2ID() {
    }

    public Rect2ID(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public Rect2ID(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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

    public int getLeft() {
        return x;
    }

    public int getTop() {
        return y;
    }

    public int getRight() {
        return x + w;
    }

    public int getBottom() {
        return y + h;
    }

    public void setBounds(int l, int t, int r, int b) {
        this.x = l;
        this.y = t;
        this.w = r - l;
        this.h = b - t;
    }

    public void setRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

}
