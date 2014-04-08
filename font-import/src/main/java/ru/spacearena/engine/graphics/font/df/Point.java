package ru.spacearena.engine.graphics.font.df;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class Point {

    public int x, y;
    public float d;

    public Point(float d, int x, int y) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y, float d) {
        this.d = d;
        this.x = x;
        this.y = y;
    }

}
