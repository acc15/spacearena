package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.shaders.PositionProgram;
import ru.spacearena.engine.graphics.shaders.Program;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.graphics.vbo.VBODefinition;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.HashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext {

    public static final VBODefinition SIN_COS_VBO = new VBODefinition(
            OpenGL.BufferType.ARRAY, OpenGL.BufferUsage.STATIC_DRAW);

    public static final int MAX_MATRIX_DEPTH = 40;
    public static final int MAX_VERTEX_COUNT = 100;

    private final OpenGL gl;

    private final VertexBuffer vertexBuffer = new VertexBuffer(MAX_VERTEX_COUNT * 2);

    private final Matrix activeMatrix = new Matrix();
    private final float[] matrixStack = new float[Matrix.ELEMENTS_PER_MATRIX * MAX_MATRIX_DEPTH];
    private int matrixIndex = 0;

    private final HashMap<Program.Definition, Program> programs =
            new HashMap<Program.Definition, Program>();

    private final HashMap<VBODefinition, VertexBufferObject> vbos = new HashMap<VBODefinition, VertexBufferObject>();

    private final Binder binder = new Binder();

    public DrawContext(OpenGL gl) {
        this.gl = gl;
        register(PositionProgram.DEFINITION);
    }

    public void pushMatrix(Matrix m) {
        activeMatrix.toArrayCompact(matrixStack, matrixIndex);
        activeMatrix.postMultiply(m);
        matrixIndex += Matrix.ELEMENTS_PER_MATRIX;
    }

    public void popMatrix() {
        if (matrixIndex <= 0) {
            throw new IllegalStateException("Empty matrix stack");
        }
        matrixIndex -= Matrix.ELEMENTS_PER_MATRIX;
        activeMatrix.fromArrayCompact(matrixStack, matrixIndex);
    }

    public void clear(Color color) {
        gl.clearColor(color.r, color.g, color.b, color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void init() {
        fillNGonBuf(MAX_VERTEX_COUNT, 0, 0, 1, 1);
        uploadVBO(SIN_COS_VBO, vertexBuffer);
    }

    public void dispose() {
        for (Program p : programs.values()) {
            p.markDead();
        }
        for (VertexBufferObject vbo : vbos.values()) {
            vbo.markDead();
        }
    }

    public Matrix getActiveMatrix() {
        return activeMatrix;
    }

    public Program register(Program.Definition def) {
        Program p = programs.get(def);
        if (p != null) {
            return p;
        }
        p = def.createProgram();
        programs.put(def, p);
        return p;
    }

    public void unregister(Program.Definition def) {
        final Program p = programs.get(def);
        if (p == null) {
            return;
        }
        p.delete(gl);
    }

    public Binder use(Program.Definition def) {
        return binder.use(register(def));
    }

    public VertexBufferObject getVBO(VBODefinition definition) {
        final VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            throw new IllegalArgumentException("VBO with definition " + definition + " doesn't exists in context");
        }
        return vbo;
    }

    public VertexBufferObject uploadVBO(VertexBufferObject vbo, VertexBuffer buffer) {
        if (!vbos.containsKey(vbo) && vbo.isCreated()) {
            throw new IllegalArgumentException("Can't use one VBO in different contexts");
        }
        vbo.upload(gl, buffer);
        vbos.put(vbo, vbo);
        return vbo;
    }

    public VertexBufferObject uploadVBO(VBODefinition definition, VertexBuffer buffer) {
        VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            vbo = new VertexBufferObject(definition.getBufferType(), definition.getBufferUsage());
        }
        vbo.upload(gl, buffer);
        vbos.put(definition, vbo);
        return vbo;
    }

    public void deleteVBO(VBODefinition definition) {
        final VertexBufferObject vbo = getVBO(definition);
        vbos.remove(definition);
        vbo.delete(gl);
    }

    private void drawBuf(OpenGL.PrimitiveType type, Color color) {
        use(PositionProgram.DEFINITION).
                bindAttr(PositionProgram.POSITION_ATTR, vertexBuffer, 0).
                bindUniform(PositionProgram.COLOR_UNIFORM, color).
                bindUniform(PositionProgram.MATRIX_UNIFORM, activeMatrix).
                draw(type);
    }

    private void fillNGonBuf(int n, float x, float y, float rx, float ry) {
        if (n < 3) {
            throw new IllegalArgumentException("N-Gon should have at least 3 points");
        }
        if (n > MAX_VERTEX_COUNT) {
            n = MAX_VERTEX_COUNT;
        }

        final float a = FloatMathUtils.TWO_PI / n;
        final float c = FloatMathUtils.cos(a), s = FloatMathUtils.sin(a);
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2);
        float vx = 1, vy = 0;
        for (int i = 0; i < n; i++) {
            vertexBuffer.put(x + vx * rx, y + vy * ry);
            final float ny = vx * s + vy * c;
            vx = vx * c - vy * s;
            vy = ny;
        }
    }

    public void fillNGon(int n, float x, float y, float rx, float ry, Color color) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_FAN, color);
    }

    public void drawNGon(int n, float x, float y, float rx, float ry, Color color) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color);
    }

    public void fillConvexPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_FAN, color);
    }

    public void drawPoly(float[] points, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(points);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color);
    }

    public void drawPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color);
    }

    public void fillRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(x1, y2).put(x2, y2).put(x1, y1).put(x2, y1);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_STRIP, color);
    }

    public void drawRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color);
    }

    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset().layout(PositionProgram.LAYOUT_P2).put(x1, y1).put(x2, y2);
        drawBuf(OpenGL.PrimitiveType.LINES, color);
    }

    private void renderEllipse(OpenGL.PrimitiveType type, float x, float y, float rx, float ry, Color color) {
        // storing current matrix on stack... bad approach
        final float m0 = activeMatrix.m[0], m1 = activeMatrix.m[1], m4 = activeMatrix.m[4], m5 = activeMatrix.m[5],
                    m12 = activeMatrix.m[12],m13 = activeMatrix.m[13];
        activeMatrix.postTranslate(x, y);
        activeMatrix.postScale(rx, ry);
        use(PositionProgram.DEFINITION).
                bindAttr(PositionProgram.POSITION_ATTR, SIN_COS_VBO, 0).
                bindUniform(PositionProgram.COLOR_UNIFORM, color).
                bindUniform(PositionProgram.MATRIX_UNIFORM, activeMatrix).
                draw(type);
        activeMatrix.set(m0, m1, m4, m5, m12, m13);
    }


    public void fillEllipse(float x, float y, float rx, float ry, Color color) {
        renderEllipse(OpenGL.PrimitiveType.TRIANGLE_FAN, x, y, rx, ry, color);
    }

    public void drawEllipse(float x, float y, float rx, float ry, Color color) {
        renderEllipse(OpenGL.PrimitiveType.LINE_LOOP, x, y, rx, ry, color);
    }

    public void fillCircle(float x, float y, float r, Color color) {
        renderEllipse(OpenGL.PrimitiveType.TRIANGLE_FAN, x, y, r, r, color);
    }

    public void drawCircle(float x, float y, float r, Color color) {
        renderEllipse(OpenGL.PrimitiveType.LINE_LOOP, x, y, r, r, color);
    }

    public void setLineWidth(float width) {
        gl.lineWidth(width);
    }

    public class Binder {

        private Program program;
        private int vertexCount = -1;

        public Binder bindUniform(int index, Point2F point) {
            gl.uniform(program.getUniformLocation(index), point.x, point.y);
            return this;
        }

        public Binder bindUniform(int index, Matrix matrix) {
            gl.uniformMatrix4(program.getUniformLocation(index), 1, matrix.m, 0);
            return this;
        }

        public Binder bindUniform(int index, Color color) {
            gl.uniform(program.getUniformLocation(index), color.r, color.g, color.b, color.a);
            return this;
        }

        public Binder bindAttr(int index, VBODefinition vboDefinition, int item) {
            final VertexBufferObject vbo = getVBO(vboDefinition);
            final int sizeInBytes = vbo.getSizeInBytes(),
                    stride = vbo.getLayout().getStride(),
                    floatCount = vbo.getLayout().getCount(item, OpenGL.Type.FLOAT),
                    offsetInBytes = vbo.getLayout().getOffsetInBytes(item);

            vbo.bind(gl);
            gl.vertexAttribPointer(index, floatCount, OpenGL.Type.FLOAT, false, stride, offsetInBytes);
            gl.enableVertexAttribArray(index);
            adjustVertexCount(sizeInBytes, stride);
            return this;
        }

        public Binder bindAttr(int index, VertexBuffer buffer, int item) {
            final int sizeInBytes = buffer.getSizeInBytes(),
                    stride = buffer.getLayout().getStride(),
                    floatCount = buffer.getLayout().getCount(item, OpenGL.Type.FLOAT);
            gl.vertexAttribPointer(index, floatCount, OpenGL.Type.FLOAT, false, stride, buffer.prepareBuffer(item));
            gl.enableVertexAttribArray(index);
            adjustVertexCount(sizeInBytes, stride);
            return this;
        }

        public void draw(OpenGL.PrimitiveType type) {
            gl.drawArrays(type, 0, vertexCount);
        }

        public void draw(OpenGL.PrimitiveType type, int count) {
            gl.drawArrays(type, 0, count);
        }

        public void draw(OpenGL.PrimitiveType type, int start, int count) {
            gl.drawArrays(type, start, count);
        }

        private void adjustVertexCount(int bufferSize, int stride) {
            final int count = bufferSize / stride;
            vertexCount = (vertexCount < 0 ? count : Math.min(vertexCount, count));
        }

        public Binder use(Program program) {
            this.vertexCount = -1;
            if (this.program == program) {
                return binder;
            }
            program.make(gl);
            gl.useProgram(program.getId());
            this.program = program;
            return binder;
        }
    }

}
