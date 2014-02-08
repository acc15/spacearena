package ru.spacearena.game;

import org.junit.Test;

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
    public void testMod() throws Exception {

        final int grid = 5;
        for (int i=-20; i<=20; i++) {
            final int v = i > 0 ? i + grid-1 : i;
            final int x = v - v % grid;
            System.out.println("Grid " + i + ": " + x);
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
