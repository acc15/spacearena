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

}
