package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class Rect2ID implements BoundingBox2I {

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

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
