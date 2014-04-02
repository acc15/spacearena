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

    public void fillRect(Color color, float x1, float y1, float x2, float y2) {

    }

    public void drawRect(Color color, float x1, float y1, float x2, float y2) {
        /*buf.put(x1).put(y1).
            put(x1).put(y2).
            put(x2).put(y2).
            put(x2).put(y1);
        PositionColorProgram2f.getInstance().use(gl).
                bind(PositionColorProgram2f.POSITION, buf).
                bind(PositionColorProgram2f.COLOR, color).
                bind(PositionColorProgram2f.MATRIX, activeMatrix.m).
                draw(OpenGL.PrimitiveType.LINES, 0, 2);*/
    }

    public void drawLine(Color color, float x1, float y1, float x2, float y2) {

        /*final FloatBuffer buf = ByteBuffer.allocateDirect(100).order(ByteOrder.nativeOrder()).asFloatBuffer();
        PositionColorProgram2f.getInstance().use(gl).
                bind(PositionColorProgram2f.POSITION, buf).
                bind(PositionColorProgram2f.COLOR, color).
                bind(PositionColorProgram2f.MATRIX, activeMatrix.m).
                draw(OpenGL.PrimitiveType.LINES, 0, 2);*/

//        gl.uniform(PositionColorProgram2f.getInstance().getUniformLocation(PositionColorProgram2f.COLOR));
//        gl.useProgram(PositionColorProgram2f.getInstance().getId());
//
//        gl.uniform4(, 1, );
//        gl.drawArrays(OpenGL.PrimitiveType.LINES, 0, 2);
    }

}
