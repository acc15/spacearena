package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public class Rect2IP extends AbstractRect2I {

    public int l = 0, t = 0, r = 0, b = 0;

    public Rect2IP() {
    }

    public Rect2IP(int r, int b) {
        setBounds(0,0,r,b);
    }

    public Rect2IP(int l, int t, int r, int b) {
        setBounds(l,t,r,b);
    }

    public void setPosition(int x, int y) {
        setBounds(x,y,x+r-l,y+b-t);
    }

    public int getWidth() {
        return r - l;
    }

    public int getHeight() {
        return b - t;
    }

    public int getLeft() {
        return l;
    }

    public int getTop() {
        return t;
    }

    public int getRight() {
        return r;
    }

    public int getBottom() {
        return b;
    }

    public void setBounds(int l, int t, int r, int b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }

    public void setRect(int x, int y, int w, int h) {
        this.l = x;
        this.t = y;
        this.r = x + w;
        this.b = y + h;
    }
}
