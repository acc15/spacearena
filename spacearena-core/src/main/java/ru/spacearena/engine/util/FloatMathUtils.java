package ru.spacearena.engine.util;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FloatMathUtils {

    public static final float PI = 3.1415927f;
    public static final float HALF_PI = PI / 2;
    public static final float TWO_PI = PI * 2;
    public static final float EPSILON = 1e-5f;
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

    public static float toRadiansTop(float degrees) {
        return toRadians(degrees - 90);
    }

    public static boolean isZero(float x, float y) {
        return isEqual(x, y, 0f, 0f);
    }

    public static boolean isOne(float x, float y) {
        return isEqual(x, y, 1f, 1f);
    }

    public static boolean isEqual(float x1, float y1, float x2, float y2) {
        return near(x1, x2) && near(y1, y2);
    }

    public static boolean isZero(float v) {
        return near(v, 0f);
    }

    public static boolean isOne(float v) {
        return near(v, 1f);
    }

    public static boolean near(float v1, float v2) {
        return near(v1, v2, EPSILON);
    }

    public static boolean near(float v1, float v2, float epsilon) {
        return Math.abs(v1 - v2) < epsilon;
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
        return sqrt(lengthSquare(x, y));
    }

    public static float scaledLength(float l, float x, float y) { return l / length(x,y); }

    public static float radDiff(float a, float b) {
        final float d = a - b;
        return d >= PI ? d - TWO_PI : d <= -PI ? TWO_PI + d : d;
    }

    /**
     * Calculates shortest difference between two angles.
     * @param a first angle in degrees (0..360)
     * @param b second angle in degrees (0..360)
     * @return difference which {@code a - diff = b}
     */
    public static float degreeDiff(float a, float b) {
        final float d = a - b;
        return d >= HALF_CIRCLE_ANGLE ? d - CIRCLE_ANGLE :
               d <= -HALF_CIRCLE_ANGLE ? CIRCLE_ANGLE + d :
               d;
    }

    public static float abs(float v) {
        return v < 0 ? -v : v;
    }

    /**
     * Returns angle of vector in radians. Synonym for {@link #atan2}.
     *
     * <pre>
     *            | -PI/2
     *            |
     *            |
     * -PI        |         0
     *  ----------|--------->
     *  PI        |
     *            |
     *            |
     *            | PI/2
     *            V
     * </pre>
     *
     * @param x v1.x
     * @param y v1.y
     * @return angle of vector in radians
     */
    public static float radians(float x, float y) { return atan2(y, x); }

    /**
     * Returns angle of vector in degrees
     * @param x v1.x
     * @param y v1.y
     * @return angle of vector in degrees
     */
    public static float degrees(float x, float y) {
        return toDegrees(radians(x,y));
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

    public static float startPos(float v, float grid) {
        return ceil(v/grid)*grid;
    }

    public static float endPos(float v, float grid) {
        return floor(v/grid)*grid;
    }

    public static boolean inRange(float v, float min, float max) {
        return v >= min && v <= max;
    }

    /**
     * Checks whether two vectors are collinear
     * @param x1 v1.x
     * @param y1 v1.y
     * @param x2 v2.x
     * @param y2 v2.y
     * @return <code>true</code> if two vectors are collinear or very close to be collinear,
     *         <code>false</code> otherwise
     */
    public static boolean isCollinear(float x1, float y1, float x2, float y2) {
        return near(x1 * y2, x2 * y1);
    }

    public static float pmod(float v, float m) {
        return v < 0 ? m + v % m : v % m;
    }

    public static float normalizeDegrees(float d) {
        return pmod(d, CIRCLE_ANGLE);
    }

    public static float normalizeRadians(float v) {
        return pmod(v, TWO_PI);
    }

    public static float copySign(float magnitude, float sign) {
        return Math.copySign(magnitude, sign);
    }

    public static float signum(float diff) {
        return Math.signum(diff);
    }

    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static float center(float a, float b) { return (a + b) / 2; }

    public static boolean absGt(float a, float b) {
        return abs(a) > abs(b);
    }

    public static int absCmp(float a, float b) {
        return Float.compare(abs(a), abs(b));
    }

    /**
     * Returns the center of two projections
     * @param min1 min value of first projection
     * @param max1 max value of first projection
     * @param min2 min value of second projection
     * @param max2 max value of second projection
     * @return the center of two projections
     */
    public static float center(float min1, float max1, float min2, float max2) {
        return center(max(min1, min2), min(max1, max2));
    }

    public static float overlap(float min1, float max1, float min2, float max2) {
        return min(max1, max2) - max(min1, min2);
    }

    public static boolean isOverlaps(float min1, float max1, float min2, float max2) {
        return overlap(min1, max1, min2, max2) >= 0;
    }

    public static float size(float min, float max) {
        return max - min;
    }

    public static float halfSize(float min, float max) {
        return size(min, max) / 2;
    }

    public static int round(float v) {
        return Math.round(v);
    }

    public static float sq(float v) {
        return v*v;
    }

    public static float addMagnitude(float v, float a) {
        return v < 0 ? v - a : v + a;
    }

    public static float clamp(float v, float min, float max) {
        return v < min ? min : v > max ? max : v;
    }

    public static float zdiv(float v, float d) {
        return isZero(d) ? 0 : v/d;
    }
}
