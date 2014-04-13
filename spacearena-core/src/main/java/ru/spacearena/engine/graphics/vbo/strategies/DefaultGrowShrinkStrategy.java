package ru.spacearena.engine.graphics.vbo.strategies;

/**
* @author Vyacheslav Mayorov
* @since 2014-14-04
*/
public class DefaultGrowShrinkStrategy implements GrowShrinkStrategy {
    private final int shrinkQueryCount;
    private final int shrinkCapacityLimit;
    private int queryCount = 0;
    private int maxRequiredCapacity = 0;

    private int reset(int returnCapacity) {
        queryCount = 0;
        maxRequiredCapacity = 0;
        return returnCapacity;
    }

    public DefaultGrowShrinkStrategy() {
        this(15, 16);
    }

    public DefaultGrowShrinkStrategy(int shrinkQueryCount, int shrinkCapacityLimit) {
        this.shrinkQueryCount = shrinkQueryCount;
        this.shrinkCapacityLimit = shrinkCapacityLimit;
    }

    public int computeCapacity(int currentCapacity, int requiredCapacity) {
        if (requiredCapacity > currentCapacity) {
            return reset(requiredCapacity << 1);
        }
        if (currentCapacity - requiredCapacity <= shrinkCapacityLimit) {
            return reset(requiredCapacity);
        }
        ++queryCount;
        maxRequiredCapacity = Math.max(maxRequiredCapacity, requiredCapacity);
        if (queryCount <= shrinkQueryCount) {
            return currentCapacity;
        }
        return reset(maxRequiredCapacity);
    }
}
