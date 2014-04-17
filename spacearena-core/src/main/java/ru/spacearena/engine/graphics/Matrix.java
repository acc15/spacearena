package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils;

import static ru.spacearena.engine.util.FloatMathUtils.isEqual;
import static ru.spacearena.engine.util.FloatMathUtils.isZero;

/**
 * Implementation is optimized for 2D graphics and therefore assumes
 * that elements on the two lower rows are equal to rows in identity matrix.
 * Only following elements are taken into account when performing matrix multiplications:
 *
 * <pre>
 * m0  m4  0   m12
 * m1  m5  0   m13
 * 0   0   1   0
 * 0   0   0   1
 * </pre>
 *
 * The matrix is 4x4 array - only just to be compatible with OpenGL 2 [ES 2] graphics pipeline
 * (vertex shader in essence)
 *
* @author Vyacheslav Mayorov
* @since 2014-31-03
*/
public class Matrix {

    public static final int ELEMENTS_PER_MATRIX = 6;

    public final float[] m = new float[] {
        1,0,0,0,
        0,1,0,0,
        0,0,1,0,
        0,0,0,1
    };

    public Matrix() {
    }

    public Matrix(float[] m) {
        set(m);
    }

    public Matrix(Matrix m) {
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

    public void preMultiply(Matrix a) {
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

    public void postMultiply(Matrix a) {
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

    public void set(Matrix matrix) {
        m[0] = matrix.m[0];
        m[1] = matrix.m[1];
        m[4] = matrix.m[4];
        m[5] = matrix.m[5];
        m[12] = matrix.m[12];
        m[13] = matrix.m[13];
    }

    /**
     * Same as <pre>
     *     identity();
     *     preRotate(nx, ny);
     *     preTranslate(tx, ty);
     * </pre>
     * @param nx rotate normal X
     * @param ny rotate normal Y
     * @param tx translate X
     * @param ty translate Y
     */
    public void setTransform(float nx, float ny, float tx, float ty) {
        set(nx,ny,-ny,nx,tx,ty);
    }

    /**
     * Same as <pre>
     *     identity();
     *     preRotate(angrad);
     *     preTranslate(tx, ty);
     * </pre>
     * @param angrad rotate radians
     * @param tx translate X
     * @param ty translate Y
     */
    public void setTransform(float angrad, float tx, float ty) {
        setTransform(FloatMathUtils.cos(angrad), FloatMathUtils.sin(angrad), tx, ty);
    }

    /**
     * Same as <pre>
     *     identity();
     *     preTranslate(-px, -py);
     *     preScale(sx, sy);
     *     preShear(kx, ky);
     *     preRotate(angrad);
     *     preTranslate(tx, ty);
     * </pre>
     * @param px pivot X
     * @param py pivot Y
     * @param sx scale X
     * @param sy scale Y
     * @param kx shear X
     * @param ky shear Y
     * @param angrad rotate radians
     * @param tx translate X
     * @param ty translate Y
     */
    public void setTransform(float px, float py,
                             float sx, float sy,
                             float kx, float ky,
                             float angrad,
                             float tx, float ty) {
        setTransform(px, py, sx, sy, kx, ky, FloatMathUtils.cos(angrad), FloatMathUtils.sin(angrad), tx, ty);
    }

    /**
     * Same as <pre>
     *     identity();
     *     preTranslate(-px, -py);
     *     preScale(sx, sy);
     *     preShear(kx, ky);
     *     preRotate(nx, ny);
     *     preTranslate(tx, ty);
     * </pre>
     * @param px pivot X
     * @param py pivot Y
     * @param sx scale X
     * @param sy scale Y
     * @param kx shear X
     * @param ky shear Y
     * @param nx rotate normal X
     * @param ny rotate normal Y
     * @param tx translate X
     * @param ty translate Y
     */
    public void setTransform(float px, float py,
                             float sx, float sy,
                             float kx, float ky,
                             float nx, float ny,
                             float tx, float ty) {
        identity();
        preTranslate(-px, -py);
        preScale(sx, sy);
        preShear(kx, ky);
        preRotate(nx, ny);
        preTranslate(tx, ty);
//        set(nx*sx - ny*ky*sx,
//            ny*sx + nx*ky*sx,
//            nx*kx*sy - ny*sy,
//            ny*kx*sy + nx*sy,
//            tx + ny*ky*sx*px - nx*sx*px - kx*sy*py - sy*py,
//            ty - nx*ky*sx*px - ny*sx*px - kx*sy*py - sy*py);
    }

    public float transformVectorX(float x, float y) { return m[0] * x + m[4] * y; }

    public float transformVectorY(float x, float y) { return m[1] * x + m[5] * y; }

    public float transformPointX(float x, float y) {
        return m[0] * x + m[4] * y + m[12];
    }

    public float transformPointY(float x, float y) {
        return m[1] * x + m[5] * y + m[13];
    }

    public boolean isIdentity() {
        return FloatMathUtils.isOne(m[0]) && FloatMathUtils.isZero(m[1]) &&
               FloatMathUtils.isZero(m[4]) && FloatMathUtils.isOne(m[5]) &&
               FloatMathUtils.isZero(m[12]) && FloatMathUtils.isZero(m[13]);
    }

    public float invertTransformVectorX(float x, float y, float det) {
        return (x * m[5] - y * m[4])/det;
    }

    public float invertTransformVectorY(float x, float y, float det) {
        return (y * m[0] - x * m[1])/det;
    }

    public float invertTransformPointX(float x, float y, float det) {
        return (x * m[5] - y * m[4] + m[4]*m[13] - m[5]*m[12])/det;
    }

    public float invertTransformPointY(float x, float y, float det) {
        return (y * m[0] - x * m[1] - m[0]*m[13] + m[1]*m[12])/det;
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

    public void inverse(Matrix matrix) {
        set(matrix);
        invert();
    }

    public float determinant() {
        return m[0]*m[5]-m[1]*m[4];
    }

    public boolean isInvertible() {
        return !isZero(determinant());
    }

    public boolean isCloseTo(Matrix v) {
        return isEqual(m[0], v.m[0]) && isEqual(m[1], v.m[1]) &&
                isEqual(m[4], v.m[4]) && isEqual(m[5], v.m[5]) &&
                isEqual(m[12], v.m[12]) && isEqual(m[13], v.m[13]);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Matrix && equals((Matrix)obj);
    }

    public boolean equals(Matrix v) {
        return m[0] == v.m[0] &&
               m[1] == v.m[1] &&
               m[4] == v.m[4] &&
               m[5] == v.m[5] &&
               m[12] == v.m[12] &&
               m[13] == v.m[13];
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

    /* POINT TRANSFORM HELPERS */

    public Point2F transformPoint(float x, float y, Point2F out) {
        out.x = transformPointX(x, y);
        out.y = transformPointY(x, y);
        return out;
    }

    public Point2F transformPoint(float x, float y) {
        return transformPoint(x, y, new Point2F());
    }

    public Point2F transformPoint(Point2F pt, Point2F out) {
        return transformPoint(pt.x, pt.y, out);
    }

    public Point2F transformPoint(Point2F pt) {
        return transformPoint(pt, pt);
    }


    /* VECTOR TRANSFORM HELPERS */

    public Point2F transformVector(float x, float y, Point2F out) {
        out.x = transformVectorX(x, y);
        out.y = transformVectorY(x, y);
        return out;
    }

    public Point2F transformVector(float x, float y) {
        return transformVector(x, y, new Point2F());
    }

    public Point2F transformVector(Point2F pt, Point2F out) {
        return transformVector(pt.x, pt.y, out);
    }

    public Point2F transformVector(Point2F pt) {
        return transformVector(pt, pt);
    }

    /* INVERT POINT TRANSFORM HELPERS */
    public float invertTransformPointX(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformPointX(x, y, det);
    }

    public float invertTransformPointY(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformPointY(x, y, det);
    }

    public Point2F invertTransformPoint(float x, float y, Point2F out) {
        final float det = determinant();
        checkDeterminant(det);
        out.x = invertTransformPointX(x, y, det);
        out.y = invertTransformPointY(x, y, det);
        return out;
    }

    public Point2F invertTransformPoint(float x, float y) {
        return invertTransformPoint(x, y, new Point2F());
    }

    public Point2F invertTransformPoint(Point2F pt, Point2F out) {
        return invertTransformPoint(pt.x, pt.y, out);
    }

    public Point2F invertTransformPoint(Point2F pt) {
        return invertTransformPoint(pt, pt);
    }

    /* INVERT VECTOR TRANSFORM HELPERS */
    public float invertTransformVectorX(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformVectorX(x, y, det);
    }

    public float invertTransformVectorY(float x, float y) {
        final float det = determinant();
        checkDeterminant(det);
        return invertTransformVectorY(x, y, det);
    }

    public Point2F invertTransformVector(float x, float y, Point2F out) {
        final float det = determinant();
        checkDeterminant(det);
        out.x = invertTransformVectorX(x, y, det);
        out.y = invertTransformVectorY(x, y, det);
        return out;
    }

    public Point2F invertTransformVector(float x, float y) {
        return invertTransformVector(x, y, new Point2F());
    }

    public Point2F invertTransformVector(Point2F pt, Point2F out) {
        return invertTransformVector(pt.x, pt.y, out);
    }

    public Point2F invertTransformVector(Point2F pt) {
        return invertTransformVector(pt, pt);
    }

    public void transformPoints(float[] src, int srcOffset, float[] dst, int dstOffset, int count) {
        for (int i=0; i<count; i++) {
            final float srcX = src[i*2+srcOffset];
            final float srcY = src[i*2+srcOffset+1];
            final float dstX = transformPointX(srcX, srcY);
            final float dstY = transformPointY(srcX, srcY);
            dst[i*2+dstOffset] = dstX;
            dst[i*2+dstOffset+1] = dstY;
        }
    }

    public void fromArrayCompact(float[] a, int offset) {
        m[0]  = a[  offset];
        m[1]  = a[++offset];
        m[4]  = a[++offset];
        m[5]  = a[++offset];
        m[12] = a[++offset];
        m[13] = a[++offset];
    }

    public void toArrayCompact(float[] a, int offset) {
        a[  offset] = m[0];
        a[++offset] = m[1];
        a[++offset] = m[4];
        a[++offset] = m[5];
        a[++offset] = m[12];
        a[++offset] = m[13];
    }

}
