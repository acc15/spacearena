package ru.spacearena.engine.graphics.vbo.strategies;

/**
* @author Vyacheslav Mayorov
* @since 2014-14-04
*/
public interface GrowShrinkStrategy {
    int computeCapacity(int currentCapacity, int requiredCapacity);
}
