package ru.spacearena.android.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FloatMathUtils {


    public static final float PI = 3.1415927f;
    public static final float TWO_PI = PI * 2;
    public static final float EPSILON = 0.00001f;
    public static final float HALF_CIRCLE_ANGLE = 180;
    public static final float CIRCLE_ANGLE = HALF_CIRCLE_ANGLE * 2;

    public static final float RAD_TO_DEG = HALF_CIRCLE_ANGLE / PI;
    public static final float DEG_TO_RAD = PI / HALF_CIRCLE_ANGLE;

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
        return radians * RAD_TO_DEG;
    }

    public static float toRadians(float degrees) {
        return degrees * DEG_TO_RAD;
    }

    public static boolean isZero(float x, float y) {
        return isEqual(x, y, 0f, 0f);
    }

    public static boolean isOne(float x, float y) {
        return isEqual(x, y, 1f, 1f);
    }

    public static boolean isEqual(float x1, float y1, float x2, float y2) {
        return isEqual(x1, x2) && isEqual(y1, y2);
    }

    public static boolean isZero(float v) {
        return isEqual(v, 0f);
    }

    public static boolean isOne(float v) {
        return isEqual(v, 1f);
    }

    public static boolean isEqual(float v1, float v2) {
        return isEqual(v1, v2, EPSILON);
    }

    public static boolean isEqual(float v1, float v2, float delta) {
        return Math.abs(v1 - v2) < delta;
    }

    public static float floor(float v) {
        return (float)Math.floor(v);
    }

    public static float ceil(float v) {
        return (float)Math.ceil(v);
    }

    public static float sqrt(float v) {
        return (float)Math.sqrt(v);
    }

    public static float lengthSquare(float x, float y) {
        return x * x + y * y;
    }

    public static float length(float x, float y) {
        return sqrt(lengthSquare(x,y));
    }

    public static float angle(float x, float y) {
        return toDegrees(atan2(-x,y))+HALF_CIRCLE_ANGLE;
    }

    public static float angle(float x1, float y1, float x2, float y2) {
        return atan2(cross(x1, y1, x2, y2), dot(x1, y1, x2, y2));
    }

    public static float cross(float x1, float y1, float x2, float y2) {
        return x1 * y2 - x2 * y1;
    }

    public static float dot(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    public static float firstVisiblePosition(float v, float grid) {
        return ceil(v/grid)*grid;
    }

    public static float normalizeDegrees(float degrees) {
        float norm = degrees % CIRCLE_ANGLE;
        if (norm < 0) {
            return CIRCLE_ANGLE + norm;
        }
        return norm;
    }
}