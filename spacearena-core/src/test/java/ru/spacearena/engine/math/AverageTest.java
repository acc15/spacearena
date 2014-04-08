package ru.spacearena.engine.math;

import org.junit.Test;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;

import java.math.BigDecimal;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class AverageTest {
    @Test
    public void testCount() throws Exception {
        final Average a = new Average();

        final float[] v = new float[1000];

        float maxError = 0;

        for (int i=0; i<100; i++) {

            for (int m = 0; m < v.length; m++) {
                v[m] = TempUtils.RAND.nextFloatBetween(-1000, 1000);
            }

            BigDecimal sum = new BigDecimal(0);
            for (float aV : v) {
                sum = sum.add(new BigDecimal(aV));
            }
            final BigDecimal expectedAverage = sum.divide(new BigDecimal(v.length));
            final float closeToTruth = expectedAverage.floatValue();

            a.reset();
            for (float av : v) {
                a.count(av);
            }

            final float diff = FloatMathUtils.abs(closeToTruth - a.getAverage());
            if (diff > maxError) {
                maxError = diff;
            }
        }
        System.out.println("Average MaxError: " + maxError);
        assertThat(maxError).isLessThan(0.0001f);

    }
}
