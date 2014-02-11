package ru.spacearena.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FloatMathUtils {


    public static final float PI = 3.1415927f;
    public static final float DELTA_PRECISION = 0.0001f;

    public static float atan2(float y, float x) {
        return (float)Math.atan2(y, x);
    }

    public static float sin(float v) {
        return (float)Math.sin(v);
    }

    public static float cos(float v) {
        return (float)Math.cos(v);
    }

    public static float toDegrees(float radians) {
        return radians * 180 / PI;
    }

    public static float toRadians(float degrees) {
        return degrees * PI / 180;
    }

    public static boolean isEqual(float v1, float v2) {
        return isEqual(v1, v2, DELTA_PRECISION);
    }

    public static boolean isEqual(float v1, float v2, float delta) {
        return Math.abs(v1 - v2) < delta;
    }

    public static float sqrt(float v) {
        return (float)Math.sqrt(v);
    }
}
