package ru.spacearena.engine.util;

import org.junit.Test;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-03
 */
public class FloatMathUtilsTest {


    @Test
    public void testSinCosOrSqrt() throws Exception {

        final Timer t = new NanoTimer();

        t.start();
        for (int i=0; i<10000000; i++) {
            final float sin = FloatMathUtils.sin(i);
            final float cos = FloatMathUtils.cos(i);
        }
        final float ttrig = t.reset();


        t.start();
        for (int i=0; i<10000000; i++) {
            final float q = FloatMathUtils.sqrt(i);

        }
        final float tsqrt = t.reset();

        final float d = ttrig - tsqrt;
        System.out.println("Sec diff: " + d);
    }
}
