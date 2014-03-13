package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
public class Circle2FCR {

    public float x, y;
    public float r;

    public Circle2FCR() {
    }

    public Circle2FCR(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void set(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public float computeX(float nx) {
        return x + nx * r;
    }

    public float computeY(float ny) {
        return y + ny * r;
    }

}
