package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.shaders.PositionColorProgram;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.HashSet;

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

    private final HashSet<ShaderProgram> usedPrograms = new HashSet<ShaderProgram>();

    public DrawContext(OpenGL gl) {
        this.gl = gl;
        this.usedPrograms.add(PositionColorProgram.getInstance());
    }

    public void pushMatrix(Matrix m) {
        activeMatrix.toArrayCompact(matrixStack, matrixDepth * Matrix.ELEMENTS_PER_MATRIX);
        activeMatrix.postMultiply(m);
        ++matrixDepth;
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

    public void init() {
        for (ShaderProgram program: usedPrograms) {
            program.make(gl);
        }
    }

    public void dispose() {
        for (ShaderProgram program: usedPrograms) {
            program.delete(gl);
        }
    }

    public Matrix getActiveMatrix() {
        return activeMatrix;
    }

    public class Binder {

        private ShaderProgram activeProgram;

        private Binder use(ShaderProgram program) {
            if (program != activeProgram) {
                usedPrograms.add(program);
                program.make(gl);
                gl.useProgram(program.getId());
                activeProgram = program;
            }
            return this;
        }

        public Binder bindUniform(int index, Point2F point) {
            gl.uniform(activeProgram.getUniformLocation(index), point.x, point.y);
            return this;
        }

        public Binder bindUniform(int index, Matrix matrix) {
            gl.uniformMatrix4(activeProgram.getUniformLocation(index), 1, matrix.m, 0);
            return this;
        }

        private final float[] c = new float[4];

        public Binder bindUniform(int index, Color color) {
            gl.uniform(activeProgram.getUniformLocation(index), color.r, color.g, color.b, color.a);
            return this;
        }

        public Binder bindAttr(int index, VertexBuffer buffer, int item) {
            gl.vertexAttribPointer(index,
                    buffer.getCount(item), OpenGL.Type.FLOAT, false,
                    buffer.getStride(),
                    buffer.getBuffer(item));
            gl.enableVertexAttribArray(index);
            return this;
        }

        public void draw(OpenGL.PrimitiveType type, int count) {
            gl.drawArrays(type, 0, count);
        }

        public void draw(OpenGL.PrimitiveType type, int count, int start) {
            gl.drawArrays(type, start, count);
        }
    }

    private final Binder binder = new Binder();

    public Binder use(ShaderProgram program) {
        return binder.use(program);
    }

    private static final int MAX_VERTEX_SIZE = 100 * 2;
    private final VertexBuffer vertexBuffer = new VertexBuffer(MAX_VERTEX_SIZE);

    private void drawBuf(OpenGL.PrimitiveType type, Color color, int count) {
        use(PositionColorProgram.getInstance()).
                bindAttr(PositionColorProgram.POSITION_ATTR, vertexBuffer, 0).
                bindUniform(PositionColorProgram.COLOR_UNIFORM, color).
                bindUniform(PositionColorProgram.MATRIX_UNIFORM, activeMatrix).
                draw(type, count);
    }

    public void drawNGon(int n, float x, float y, float size, Color color) {

        if (n < 3) {
            throw new IllegalArgumentException("N-Gon should have at least 3 points");
        }

        final float a = FloatMathUtils.TWO_PI/n;
        final float c = FloatMathUtils.cos(a), s = FloatMathUtils.sin(a);

        vertexBuffer.reset().layout(2);

        float vx = 1, vy = 0;
        for (int i=0; i<n; i++) {
            vertexBuffer.put(x + vx*size, y + vy*size);
            vx = vx*c-vy*s;
            vy = vx*s+vy*c;
        }
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color, n);
    }

    public void fillPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset().layout(2).put(points, start, size);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_STRIP, color, points.length / 2);
    }

    public void drawPoly(float[] points, Color color) {
        vertexBuffer.reset().layout(2).put(points);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color, points.length / 2);
    }

    public void drawPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset().layout(2).put(points, start, size);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color, size/2);
    }

    public void fillRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(2).put(x1, y2).put(x2, y2).put(x1, y1).put(x2, y1);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_STRIP, color, 4);
    }

    public void drawRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color, 4);
    }

    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(2).put(x1,y1).put(x2, y2);
        drawBuf(OpenGL.PrimitiveType.LINES, color, 2);
    }

    public void setLineWidth(float width) {
        gl.lineWidth(width);
    }

}
