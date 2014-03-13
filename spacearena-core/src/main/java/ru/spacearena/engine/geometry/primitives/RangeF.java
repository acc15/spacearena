package ru.spacearena.engine.geometry.primitives;

import ru.spacearena.engine.util.FloatMathUtils
;

/**
* @author Vyacheslav Mayorov
* @since 2014-09-03
*/
public class RangeF {

    public float min, max;
    private boolean initialized = false;

    public void reset() {
        this.initialized = false;
    }

    public boolean isSet() {
        return this.initialized;
    }

    public void init(float value) {
        min = max = value;
    }

    public float getCenter() {
        return (min + max)/2;
    }

    public float getSize() {
        return max - min;
    }

    public float getHalfSize() {
        return getSize()/2;
    }

    public void offset(float amount) {
        this.min += amount;
        this.max += amount;
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

    public float center(RangeF range) {
        return FloatMathUtils.center(min, max, range.min, range.max);
    }

    public float overlap(RangeF range) {
        return FloatMathUtils.overlap(min, max, range.min, range.max);
    }

    public boolean overlaps(RangeF range) {
        return FloatMathUtils.isOverlaps(min, max, range.min, range.max);
    }
}
