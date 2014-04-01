package ru.spacearena.jogl.gl;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.math.Matrix2FGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class GLDrawContext {

    public static final int MAX_MATRIX_DEPTH = 40;

    private OpenGL gl;

    public final Color4F color = new Color4F();
    private final Matrix2FGL activeMatrix = new Matrix2FGL();
    private final float[] matrixStack = new float[Matrix2FGL.ELEMENTS_PER_MATRIX * MAX_MATRIX_DEPTH];
    private int matrixDepth = 0;

    public GLDrawContext(OpenGL gl) {
        this.gl = gl;
    }

    public void pushMatrix(Matrix2FGL m) {
        activeMatrix.toArrayCompact(matrixStack, matrixDepth * Matrix2FGL.ELEMENTS_PER_MATRIX);
        activeMatrix.postMultiply(m);
    }

    public void popMatrix() {
        if (matrixDepth <= 0) {
            throw new IllegalStateException("Empty matrix stack");
        }
        --matrixDepth;
        activeMatrix.fromArrayCompact(matrixStack, matrixDepth * Matrix2FGL.ELEMENTS_PER_MATRIX);
    }

    public void clear() {
        gl.clearColor(color.r,color.g,color.b,color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {


    }

}
