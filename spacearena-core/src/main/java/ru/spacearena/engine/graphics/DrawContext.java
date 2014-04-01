package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext {

    public static final int MAX_MATRIX_DEPTH = 40;

    private final OpenGL gl;

    private final Matrix activeMatrix = new Matrix();
    private final float[] matrixStack = new float[Matrix.ELEMENTS_PER_MATRIX * MAX_MATRIX_DEPTH];
    private int matrixDepth = 0;

    public DrawContext(OpenGL gl) {
        this.gl = gl;
    }

    public void pushMatrix(Matrix m) {
        activeMatrix.toArrayCompact(matrixStack, matrixDepth * Matrix.ELEMENTS_PER_MATRIX);
        activeMatrix.postMultiply(m);
    }

    public void popMatrix() {
        if (matrixDepth <= 0) {
            throw new IllegalStateException("Empty matrix stack");
        }
        --matrixDepth;
        activeMatrix.fromArrayCompact(matrixStack, matrixDepth * Matrix.ELEMENTS_PER_MATRIX);
    }

    public void clear(Color color) {
        gl.clearColor(color.r,color.g,color.b,color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void drawLine(Color color, float x1, float y1, float x2, float y2) {


    }

}
