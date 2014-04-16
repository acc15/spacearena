package ru.spacearena.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-04
 */
public class IntMathUtils {

    public static int min(int a, int b) {
        return a < b ? a : b;
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int clamp(int v, int min, int max) {
        return v < min ? min : v > max ? max : v;
    }

    public static int compare(int a, int b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }

    public static int abs(int v) { return v < 0 ? -v : v; }

    public static int pow2RoundUp (int x) {
        if (x < 0) {
            return 0;
        }
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x+1;
    }
}
