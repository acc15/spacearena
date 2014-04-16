package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
public class Ellipse2FR {

    public float x, y;
    public float rx, ry;

    public Ellipse2FR() {
    }

    public Ellipse2FR(float x, float y, float vx, float vy) {
        set(x, y, vx, vy);
    }

    public void set(float x, float y, float rx, float ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
    }

    public float computeX(float nx) {
        return x + nx * rx;
    }

    public float computeY(float ny) {
        return y + ny * ry;
    }
}
