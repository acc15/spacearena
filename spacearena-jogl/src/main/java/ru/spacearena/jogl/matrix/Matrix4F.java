package ru.spacearena.jogl.matrix;

import ru.spacearena.engine.util.FloatMathUtils;

/**
 * Implementation is optimized for 2D graphics and therefore makes assumption
 * that elements on two lower rows are the equal to identity matrix.
 * Only following elements will be taken into account when performing matrix multiplications:
 *
 * <pre>
 * A00 A01 [0] A03
 * A04 A05 [0] A07
 * [0] [0] [1] [0]
 * [0] [0] [0] [1]
 * </pre>
 *
 * The matrix is 4x4 array - only
 * just to be compatible with OpenGL 2 [ES 2] graphics pipeline (vertex shader in essence)
 *
* @author Vyacheslav Mayorov
* @since 2014-31-03
*/
public final class Matrix4F {

    public float[] m = new float[] {
        1,0,0,0,
        0,1,0,0,
        0,0,1,0,
        0,0,0,1
    };

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

    public void preMultiply(Matrix4F a) {
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

    public void set(Matrix4F matrix) {
        m[0] = matrix.m[0];
        m[1] = matrix.m[1];
        m[4] = matrix.m[4];
        m[5] = matrix.m[5];
        m[12] = matrix.m[12];
        m[13] = matrix.m[13];
    }

    public void postMultiply(Matrix4F a) {
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

    public float transformX(float x, float y) {
        return m[0] * x + m[4] * y + m[12];
    }

    public float transformY(float x, float y) {
        return m[1] * x + m[5] * y + m[13];
    }

}
