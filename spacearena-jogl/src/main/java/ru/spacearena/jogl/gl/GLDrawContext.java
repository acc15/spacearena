package ru.spacearena.jogl.gl;

import ru.spacearena.engine.math.Matrix2FGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class GLDrawContext {

    public static final int MAX_MATRIX_DEPTH = 40;

    private OpenGL gl;

    private float r,g,b,a;
    private float[] matrixStack = new float[Matrix2FGL.ELEMENTS_PER_MATRIX * MAX_MATRIX_DEPTH];
    private int matrixDepth = 0;
    private Matrix2FGL activeMatrix = new Matrix2FGL();

    public void pushMatrix(Matrix2FGL m) {
        activeMatrix.toArrayCompact(matrixStack, matrixDepth * 6);
        activeMatrix.set(m);
    }

    public void popMatrix() {
        --matrixDepth;
        activeMatrix.fromArrayCompact(matrixStack, matrixDepth * 6);
    }

    public void setColor(float r, float g, float b) {
        setColor(r,g,b,1);
    }

    public void setColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void drawRect(float x1, float y1, float x2, float y2) {

    }

    public void drawPoly(float[] points) {

    }

    public void drawLine(float x1, float y1, float x2, float y2) {

    }

    public void fillPoly(float[] points) {

    }

    public void fillRect(float x1, float y1, float x2, float y2) {

    }


}
