package ru.spacearena.engine.graphics.vbo.strategies;

import junit.framework.TestCase;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-04
 */
public class DefaultGrowShrinkStrategyTest extends TestCase {
    public void testComputeCapacity() throws Exception {

        final int shrinkQueryCount = 5;
        final int shrinkCapacityLimit = 1;
        final DefaultGrowShrinkStrategy s = new DefaultGrowShrinkStrategy(shrinkQueryCount, shrinkCapacityLimit);
        assertThat(s.computeCapacity(16, 32)).isEqualTo(64);

        for (int i=0; i<shrinkQueryCount; i++) {
            assertThat(s.computeCapacity(16, i + 5)).isEqualTo(16);
        }
        assertThat(s.computeCapacity(16, 8)).isEqualTo(9);

    }
}
