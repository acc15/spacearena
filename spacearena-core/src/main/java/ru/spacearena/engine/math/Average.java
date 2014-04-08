package ru.spacearena.engine.math;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class Average {

    private float a;
    private int c;

    public void count(float v) {
        // another approach is to calculate on fly both gives same error
        //a = (a*c+v)/(c+1);
        a += v;
        ++c;
    }

    public float getAverage() {
        return a/c;
    }

    public void reset() {
        this.a = 0f;
        this.c = 0;
    }
}
