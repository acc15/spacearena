package ru.spacearena.engine.util;

import org.junit.Ignore;
import org.junit.Test;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-03
 */
public class FloatMathUtilsTest {


    @Test @Ignore
    public void testSinCosOrSqrt() throws Exception {

        final Timer t = new NanoTimer();

        t.start();
        for (int i=0; i<10000000; i++) {
            FloatMathUtils.sin(i);
            FloatMathUtils.cos(i);
        }
        final float ttrig = t.reset();


        t.start();
        for (int i=0; i<10000000; i++) {
            FloatMathUtils.sqrt(i);

        }
        final float tsqrt = t.reset();

        final float d = ttrig - tsqrt;
        System.out.println("Sec diff: " + d);
    }
}
