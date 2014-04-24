package ru.spacearena.engine.graphics.vbo.strategies;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class GrowOnlyStrategy implements GrowShrinkStrategy {
    public int computeCapacity(int currentCapacity, int requiredCapacity) {
        return requiredCapacity > currentCapacity ? requiredCapacity * 4 / 3 : currentCapacity;
    }
}
