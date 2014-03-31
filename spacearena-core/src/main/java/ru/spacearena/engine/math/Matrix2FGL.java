package ru.spacearena.engine.math;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils;

import static ru.spacearena.engine.util.FloatMathUtils.isEqual;
import static ru.spacearena.engine.util.FloatMathUtils.isZero;

/**
 * Implementation is optimized for 2D graphics and therefore makes assumption
 * that elements on two lower rows are the equal to identity matrix.
 * Only following elements are taken into account when performing matrix multiplications:
 *
 * <pre>
 * m0  m4  0   m12
 * m1  m5  0   m13
 * 0   0   1   0
 * 0   0   0   1
 * </pre>
 *
 * The matrix is 4x4 array - only
 * just to be compatible with OpenGL 2 [ES 2] graphics pipeline (vertex shader in essence)
 *
* @author Vyacheslav Mayorov
* @since 2014-31-03
*/
public final class Matrix2FGL {

    public static final int WORLD = 0;
    public static final int LOCAL = 1;

    public final float[] m = new float[] {
        1,0,0,0,
        0,1,0,0,
        0,0,1,0,
        0,0,0,1
    };

    public Matrix2FGL() {
    }

    public Matrix2FGL(float[] m) {
        set(m);
    }

    public Matrix2FGL(Matrix2FGL m) {
        set(m);
    }

    public void identity() {
        m[0] = 1f;
        m[1] = 0f;
        m[4] = 0f;
        m[5] = 1f;
        m[12] = 0f;
        m[13] = 0f;
    }

    public void translate(int mode, float tx, float ty) {
        switch (mode) {
        case WORLD: preTranslate(tx, ty); break;
        case LOCAL: postTranslate(tx, ty); break;
        }
    }

    public void scale(int mode, float sx, float sy) {
        switch (mode) {
        case WORLD: preScale(sx, sy); break;
        case LOCAL: postScale(sx, sy); break;
        }
    }

    public void rotate(int mode, float nx, float ny) {
        switch (mode) {
        case WORLD: preRotate(nx, ny); break;
        case LOCAL: postRotate(nx, ny); break;
        }
    }

    public void rotate(int mode, float angrad) {
        switch (mode) {
        case WORLD: preRotate(angrad); break;
        case LOCAL: postRotate(angrad); break;
        }
    }

    public void shear(int mode, float sx, float sy) {
        switch (mode) {
        case WORLD: preShear(sx, sy); break;
        case LOCAL: postShear(sx, sy); break;
        }
    }

    // PRE: M' = T*M
    // POST: M' = M*T

    public void preTranslate(float tx, float ty) {
        m[12] += tx;
        m[13] += ty;
    }

    public void postTranslate(float tx, float ty) {
        m[12] += m[0]*tx+m[4]*ty;
        m[13] += m[1]*tx+m[5]*ty;
    }

    public void preScale(float sx, float sy) {
        m[0] *= sx;
        m[1] *= sy;
        m[4] *= sx;
        m[5] *= sy;
        m[12] *= sx;
        m[13] *= sy;
    }

    public void postScale(float sx, float sy) {
        m[0] *= sx;
        m[1] *= sx;
        m[4] *= sy;
        m[5] *= sy;
    }

    public void preRotate(float nx, float ny) {
        final float m0 = m[0];
        m[0] = nx*m0-ny*m[1];
        m[1] = ny*m0+nx*m[1];

        final float m4 = m[4];
        m[4] = nx*m4-ny*m[5];
        m[5] = ny*m4+nx*m[5];

        final float m12 = m[12];
        m[12] = nx*m12-ny*m[13];
        m[13] = ny*m12+nx*m[13];
    }

    public void preRotate(float angrad) {
        preRotate(FloatMathUtils.cos(angrad), FloatMathUtils.sin(angrad));
    }

    public void postRotate(float nx, float ny) {
        final float m0 = m[0];
        m[0] = m0*nx+m[4]*ny;
        m[4] = m[4]*nx-m0*ny;

        final float m1 = m[1];
        m[1] = m1*nx+m[5]*ny;
        m[5] = m[5]*nx-m1*ny;
    }

    public void postRotate(float angrad) {
        postRotate(FloatMathUtils.cos(angrad), FloatMathUtils.sin(angrad));
    }

    public void preShear(float sx, float sy) {
        final float m0 = m[0];
        m[0] += m[1]*sx;
        m[1] += m0*sy;

        final float m4 = m[4];
        m[4] += m[5]*sx;
        m[5] += m4*sy;

        final float m12 = m[12];
        m[12] += m[13]*sx;
        m[13] += m12*sy;
    }

    public void postShear(float sx, float sy) {
        final float m0 = m[0];
        m[0] += m[4]*sy;
        m[4] += m0*sx;

        final float m1 = m[1];
        m[1] += m[5]*sy;
        m[5] += m1*sx;
    }

    public void preMultiply(Matrix2FGL a) {
        final float[] n = a.m;

        final float m0 = m[0];
        m[0] = n[0]*m0+n[4]*m[1];
        m[1] = n[1]*m0+n[5]*m[1];

        final float m4 = m[4];
        m[4] = n[0]*m4+n[4]*m[5];
        m[5] = n[1]*m4+n[5]*m[5];

        final float m12 = m[12];
        m[12] = n[0]*m12+n[4]*m[13]+n[12];
        m[13] = n[1]*m12+n[5]*m[13]+n[13];
    }

    public void postMultiply(Matrix2FGL a) {
        final float[] n = a.m;

        final float m0 = m[0], m4 = m[4];
        m[0] = m0*n[0]+m4*n[1];
        m[4] = m0*n[4]+m4*n[5];
        m[12] += m0*n[12]+m4*n[13];

        final float m1 = m[1], m5 = m[5];
        m[1] = m1*n[0]+m5*n[1];
        m[5] = m1*n[4]+m5*n[5];
        m[13] += m1*n[12]+m5*n[13];
    }

    public void set(float m0, float m1, float m4, float m5, float m12, float m13) {
        this.m[0] = m0;
        this.m[1] = m1;
        this.m[4] = m4;
        this.m[5] = m5;
        this.m[12] = m12;
        this.m[13] = m13;
    }

    public void set(float[] m) {
        this.m[0] = m[0];
        this.m[1] = m[1];
        this.m[4] = m[4];
        this.m[5] = m[5];
        this.m[12] = m[12];
        this.m[13] = m[13];
    }

    public void set(Matrix2FGL matrix) {
        m[0] = matrix.m[0];
        m[1] = matrix.m[1];
        m[4] = matrix.m[4];
        m[5] = matrix.m[5];
        m[12] = matrix.m[12];
        m[13] = matrix.m[13];
    }

    public float transformX(float x, float y) {
        return m[0] * x + m[4] * y + m[12];
    }

    public float transformY(float x, float y) {
        return m[1] * x + m[5] * y + m[13];
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

    public boolean isIdentity() {
        return FloatMathUtils.isOne(m[0]) && FloatMathUtils.isZero(m[1]) &&
               FloatMathUtils.isZero(m[4]) && FloatMathUtils.isOne(m[5]) &&
               FloatMathUtils.isZero(m[12]) && FloatMathUtils.isZero(m[13]);
    }

    public float invertTransformX(float x, float y, float det) {
        return (x * m[5] - y * m[4] + m[4]*m[13] - m[5]*m[12])/det;
    }

    public float invertTransformY(float x, float y, float det) {
        return (y * m[0] - x * m[1] - m[0]*m[13] + m[1]*m[12])/det;
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
        // 0 1 2    0 1 x 2
        // 3 4 5 -> 3 4 x 5
        // x x x    x x x x
        //          x x x x
        set(m[5]/det, -m[1]/det, -m[4]/det, m[0]/det,
            (m[4]*m[13]-m[5]*m[12])/det,
           -(m[0]*m[13]-m[1]*m[12])/det);
        return true;
    }

    public float determinant() {
        return m[0]*m[5]-m[1]*m[4];
    }

    public boolean isInvertible() {
        return !isZero(determinant());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Matrix2FGL && equals((Matrix2FGL)obj);
    }

    public boolean equals(Matrix2FGL v) {
        return isEqual(m[0], v.m[0]) && isEqual(m[1], v.m[1]) &&
               isEqual(m[4], v.m[4]) && isEqual(m[5], v.m[5]) &&
               isEqual(m[12], v.m[12]) && isEqual(m[13], v.m[13]);
    }

    @Override
    public String toString() {
        return "[" + m[0] + ", " + m[4] + ", " + m[12] + "][" + m[1] + ", " + m[5] + ", " + m[13] + "]";
    }

    private void checkDeterminant(float det) {
        if (isZero(det)) {
            throw new RuntimeException("Matrix non-invertible");
        }
    }

}
