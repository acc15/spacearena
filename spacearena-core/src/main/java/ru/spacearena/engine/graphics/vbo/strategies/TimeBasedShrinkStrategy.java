package ru.spacearena.engine.graphics.vbo.strategies;

import ru.spacearena.engine.timing.MilliTimer;
import ru.spacearena.engine.timing.Timer;

/**
* @author Vyacheslav Mayorov
* @since 2014-14-04
*/
public class TimeBasedShrinkStrategy implements GrowShrinkStrategy {
    private final float timeout;
    private final Timer timer;
    private final int valuableCapacityDiff;
    private int maxRequiredCapacity = 0;

    private int reset(int returnCapacity) {
        timer.stop();
        maxRequiredCapacity = 0;
        return returnCapacity;
    }


    public TimeBasedShrinkStrategy() {
        this(1f, 1024);
    }

    public TimeBasedShrinkStrategy(float timeout, int valuableCapacityDiff) {
        this(new MilliTimer(), timeout, valuableCapacityDiff);
    }

    public TimeBasedShrinkStrategy(Timer timer, float timeout, int valuableCapacityDiff) {
        this.timer = timer;
        this.timeout = timeout;
        this.valuableCapacityDiff = valuableCapacityDiff;
    }

    public int computeCapacity(int currentCapacity, int requiredCapacity) {
        if (requiredCapacity > currentCapacity) {
            return reset(requiredCapacity << 1);
        }
        if (currentCapacity - requiredCapacity <= valuableCapacityDiff) {
            return reset(currentCapacity);
        }
        maxRequiredCapacity = Math.max(maxRequiredCapacity, requiredCapacity);
        if (timer.lap() >= timeout) {
            return reset(maxRequiredCapacity);
        }
        return currentCapacity;
    }
}
