package ru.spacearena.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-03
 */
public class BitUtils {

    public static boolean getBit(int value, int mask) { return get(value, mask) != 0; }

    public static int setBit(int value, int mask, boolean bit) {
        return bit ? set(value, mask) : reset(value, mask);
    }

    public static int get(int value, int mask) {
        return value & mask;
    }

    public static int set(int value, int mask) {
        return value | mask;
    }

    public static int reset(int value, int mask) {
        return value & ~mask;
    }
}
