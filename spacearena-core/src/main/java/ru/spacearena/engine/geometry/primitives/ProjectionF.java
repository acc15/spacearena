package ru.spacearena.engine.geometry.primitives;

/**
* @author Vyacheslav Mayorov
* @since 2014-09-03
*/
public class ProjectionF extends RangeF {

    private boolean initialized = false;

    public void reset() {
        this.initialized = false;
    }

    public boolean isSet() {
        return this.initialized;
    }

    public void init(float value) {
        min = max = value;
        this.initialized = true;
    }

    public void accumulate(float value) {
        if (!initialized) {
            min = max = value;
            initialized = true;
            return;
        }
        if (value < min) {
            min = value;
        } else if (value > max) {
            max = value;
        }
    }

}
