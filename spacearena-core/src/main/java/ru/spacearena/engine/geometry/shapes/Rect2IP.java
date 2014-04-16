package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class Rect2IP implements BoundingBox2I {

    public int l = 0,t = 0,r = 0,b = 0;

    public Rect2IP() {
    }

    public Rect2IP(int r, int b) {
        set(0,0,r,b);
    }

    public Rect2IP(int l, int t, int r, int b) {
        set(l,t,r,b);
    }

    public void set(int l, int t, int r, int b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }

    public void moveTo(int x, int y) {
        set(x,y,x+r-l,y+b-t);
    }

    public int getWidth() {
        return r - l;
    }

    public int getHeight() {
        return b - t;
    }
}
