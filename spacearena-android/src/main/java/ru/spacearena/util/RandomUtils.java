package ru.spacearena.util;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-02
 */
public class RandomUtils {
    public static int randomBetween(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}
