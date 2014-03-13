package ru.spacearena.engine.geometry.primitives;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class ProjectionF {

    public float min, max;
    private boolean initialized = false;

    public void accumulate(float v) {
        if (!initialized) {
            min = max = v;
            initialized = true;
        }
        if (v < min) {
            min = v;
        } else if (v > max) {
            max = v;
        }
    }



}
