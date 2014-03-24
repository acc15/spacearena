package ru.spacearena.engine.random;

import org.junit.Test;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-24-03
 */
public class QRandTest {
    @Test
    public void testNextFloatPerformance() throws Exception {

        final int iterCount = 1000000000;


        final Timer timer = new NanoTimer();

        final Random random = new Random();
        timer.start();
        for (int i=0; i< iterCount; i++) {
            random.nextFloat();
        }
        final float randomTime = timer.reset();

        final QRand qRand = new QRand();
        timer.start();
        for (int i=0; i<iterCount; i++) {
            qRand.nextFloat();
        }
        final float qrandTime = timer.reset();

        System.out.printf("Random vs QRand performance [%d iterations]%n" +
                "Random.nextFloat(): %.8f%n" +
                "QRand.nextFloat(): %.8f%n" +
                "%.8f%n", iterCount, randomTime, qrandTime, randomTime/qrandTime);

        assertThat(qrandTime).isLessThan(randomTime);
    }
}
