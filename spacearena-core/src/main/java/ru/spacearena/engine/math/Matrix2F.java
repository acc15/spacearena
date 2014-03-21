package ru.spacearena.engine.math;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils;

import static ru.spacearena.engine.util.FloatMathUtils.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-03
 */
public class Matrix2F {

    public float m00 = 1f, m01 = 0f, m02 = 0f,
                 m10 = 0f, m11 = 1f, m12 = 0f;

    public Matrix2F() {
    }

    public Matrix2F(Matrix2F m) {
        set(m);
    }

    public Matrix2F(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
    }

    public void set(Matrix2F m) {
        this.m00 = m.m00;
        this.m01 = m.m01;
        this.m02 = m.m02;
        this.m10 = m.m10;
        this.m11 = m.m11;
        this.m12 = m.m12;
    }

    public void set(float m00, float m01, float m02,
                    float m10, float m11, float m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
    }

    public void identity() {
        set(1f, 0f, 0f,
            0f, 1f, 0f);
    }

    public void preTranslate(float dx, float dy) {
        m02 += dx;
        m12 += dy;
    }

    public void postTranslate(float dx, float dy) {
        m02 = m00 * dx + m01 * dy + m02;
        m12 = m10 * dx + m11 * dy + m12;
    }

    public void preScale(float sx, float sy) {
        m00 *= sx;
        m01 *= sx;
        m02 *= sx;
        m10 *= sy;
        m11 *= sy;
        m12 *= sy;
    }

    public void postScale(float sx, float sy) {
        m00 *= sx;
        m01 *= sy;
        m10 *= sx;
        m11 *= sy;
    }

    public void preRotate(float nx, float ny) {
        final float m00 = this.m00,
                    m01 = this.m01,
                    m02 = this.m02;
        this.m00 = m00 * nx - m10 * ny;
        this.m01 = m01 * nx - m11 * ny;
        this.m02 = m02 * nx - m12 * ny;
        this.m10 = m00 * ny + m10 * nx;
        this.m11 = m01 * ny + m11 * nx;
        this.m12 = m02 * ny + m12 * nx;
    }

    public void preRotate(float radians) {
        preRotate(FloatMathUtils.cos(radians), FloatMathUtils.sin(radians));
    }

    public void postRotate(float nx, float ny) {
        final float m00 = this.m00,
                    m10 = this.m10;
        this.m00 = m00 * nx + m01 * ny;
        this.m01 = m01 * nx - m00 * ny;
        this.m10 = m10 * nx + m11 * ny;
        this.m11 = m11 * nx - m10 * ny;
    }

    public void postRotate(float radians) {
        postRotate(FloatMathUtils.cos(radians), FloatMathUtils.sin(radians));
    }

    public void preMultiply(float b00, float b01, float b02, float b10, float b11, float b12) {
        multiply(b00, b01, b02, b10, b11, b12,
                 m00, m01, m02, m10, m11, m12,
                 this);
    }

    public void postMultiply(float b00, float b01, float b02, float b10, float b11, float b12) {
        multiply(m00, m01, m02, m10, m11, m12,
                 b00, b01, b02, b10, b11, b12,
                 this);
    }

    public void preMultiply(Matrix2F matrix) {
        preMultiply(matrix.m00, matrix.m01, matrix.m02, matrix.m10, matrix.m11, matrix.m12);
    }

    public void postMultiply(Matrix2F matrix) {
        postMultiply(matrix.m00, matrix.m01, matrix.m02, matrix.m10, matrix.m11, matrix.m12);
    }

    public float transformX(float x, float y) {
        return m00 * x + m01 * y + m02;
    }

    public float transformY(float x, float y) {
        return m10 * x + m11 * y + m12;
    }

    public Point2F transform(float x, float y, Point2F out) {
        out.x = transformX(x, y);
        out.y = transformY(x, y);
        return out;
    }

    public Point2F transform(float x, float y) {
        return transform(x, y, new Point2F());
    }

    public Point2F transform(Point2F pt, Point2F out) {
        return transform(pt.x, pt.y, out);
    }

    public Point2F transform(Point2F pt) {
        return transform(pt, pt);
    }

    public float invertTransformX(float x, float y, float det) {
        return (x * m11 - y * m01 + cross(m01, m02, m11, m12))/det;
    }

    public float invertTransformY(float x, float y, float det) {
        return (y * m00 - x * m10 - cross(m00, m02, m10, m12))/det;
    }

    public float invertTransformX(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformX(x, y, det);
    }

    public float invertTransformY(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformY(x, y, det);
    }

    public Point2F invertTransform(float x, float y, Point2F out) {
        final float det = determinant();
        checkDeterminant(det);
        out.x = invertTransformX(x, y, det);
        out.y = invertTransformY(x, y, det);
        return out;
    }

    public Point2F invertTransform(float x, float y) {
        return invertTransform(x, y, new Point2F());
    }

    public Point2F invertTransform(Point2F pt, Point2F out) {
        return invertTransform(pt.x, pt.y, out);
    }

    public Point2F invertTransform(Point2F pt) {
        return invertTransform(pt, pt);
    }

    public boolean invert() {
        final float det = determinant();
        if (isZero(det)) {
            return false;
        }
        set( m11/det, -m01/det,  cross(m01, m02, m11, m12)/det,
            -m10/det,  m00/det, -cross(m00, m02, m10, m12)/det);
        return true;
    }

    public float determinant() {
        return cross(m00, m01, m10, m11);
    }

    public boolean isInvertible() {
        return !isZero(determinant());
    }

    public static void multiply(Matrix2F a, Matrix2F b, Matrix2F out) {
        multiply(a.m00, a.m01, a.m02,
                 a.m10, a.m11, a.m12,
                 b.m00, b.m01, b.m02,
                 b.m10, b.m11, b.m12,
                 out);
    }

    public static void multiply(
            float a00, float a01, float a02,
            float a10, float a11, float a12,
            float b00, float b01, float b02,
            float b10, float b11, float b12,
            Matrix2F out)
    {
        out.m00 = a00 * b00 + a01 * b10;
        out.m01 = a00 * b01 + a01 * b11;
        out.m02 = a00 * b02 + a01 * b12 + a02;
        out.m10 = a10 * b00 + a11 * b10;
        out.m11 = a10 * b01 + a11 * b11;
        out.m12 = a10 * b02 + a11 * b12 + a12;
    }

    public boolean isIdentity() {
        return isOne(m00) && isZero(m01) && isZero(m02) &&
               isZero(m10) && isOne(m11) && isZero(m12);
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(m00) ^ Float.floatToIntBits(m01) ^ Float.floatToIntBits(m02) ^
               Float.floatToIntBits(m10) ^ Float.floatToIntBits(m11) ^ Float.floatToIntBits(m12);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Matrix2F && equals((Matrix2F)obj);
    }

    public boolean equals(Matrix2F m) {
        return isEqual(m00, m.m00) && isEqual(m01, m.m01) && isEqual(m02, m.m02) &&
               isEqual(m10, m.m10) && isEqual(m11, m.m11) && isEqual(m12, m.m12);
    }

    private void checkDeterminant(float det) {
        if (isZero(det)) {
            throw new RuntimeException("Matrix non-invertible");
        }
    }

}
