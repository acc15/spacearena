package ru.spacearena.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-03
 */
public class BitUtils {
    public static int get(int value, int mask) {
        return value & mask;
    }

    public static int set(int value, int mask, int bits) {
        return reset(value, mask) | bits;
    }

    public static int reset(int value, int mask) {
        return value & ~mask;
    }
}
