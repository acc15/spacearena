package ru.spacearena.game;

import org.fest.assertions.Delta;
import org.junit.Test;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-02
 */
public class SkyTest {

    private static int randomInt(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    @Test
    public void testFirstVisiblePos() throws Exception {

        final float[] expected = {
                -2,
                0,0,0,0,
                2,2,2,2,
                4,4,4,4
        };

        int e = 0;

        for (float i=-2f; i<=4f; i+=0.5f, e++) {
            final float pos = FloatMathUtils.firstVisiblePosition(i, 2);
            assertThat(pos).describedAs("for value " + i).isEqualTo(expected[e], Delta.delta(0.000001));
        }

    }

    @Test
    public void testRandom() throws Exception {

        final Random random = new Random();
        final long seed = 1;//random.nextLong();

        for (int y=-10; y<=10; y++) {
            for (int x=-10; x<=10; x++) {
                final long v = seed ^ ((long)x << 32)^y;
                random.setSeed(v);
                int x1 = randomInt(random, -200, 200);
                int y1 = randomInt(random, -200, 200);

                random.setSeed(v);
                int x2 = randomInt(random, -200, 200);
                int y2 = randomInt(random, -200, 200);

                assertThat(x1).isEqualTo(x2);
                assertThat(y2).isEqualTo(y2);

                System.out.printf("[%5d; %5d]", x1, y1);

            }
            System.out.println();
        }
    }
}
