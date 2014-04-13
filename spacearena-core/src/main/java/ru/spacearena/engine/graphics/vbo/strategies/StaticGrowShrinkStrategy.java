package ru.spacearena.engine.graphics.vbo.strategies;

/**
* @author Vyacheslav Mayorov
* @since 2014-14-04
*/
public class StaticGrowShrinkStrategy implements GrowShrinkStrategy {
    public int computeCapacity(int currentCapacity, int requiredCapacity) {
        return currentCapacity;
    }

    public static StaticGrowShrinkStrategy getInstance() {
        return instance;
    }

    public StaticGrowShrinkStrategy() {
    }

    private static final StaticGrowShrinkStrategy instance = new StaticGrowShrinkStrategy();
}
