package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.shaders.PositionColorProgram;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext {

    public static final int MAX_MATRIX_DEPTH = 40;
    private static final int MAX_VERTEX_COUNT = 100;

    private final OpenGL gl;

    private final VertexBuffer vertexBuffer = new VertexBuffer(MAX_VERTEX_COUNT*2);

    private final Matrix activeMatrix = new Matrix();
    private final float[] matrixStack = new float[Matrix.ELEMENTS_PER_MATRIX * MAX_MATRIX_DEPTH];
    private int matrixIndex = 0;

    private final HashMap<ShaderProgram.Definition, ShaderProgram> programs =
            new HashMap<ShaderProgram.Definition, ShaderProgram>();

    private final HashSet<VertexBufferObject> vbos = new HashSet<VertexBufferObject>();

    private final Binder binder = new Binder();

    public DrawContext(OpenGL gl) {
        this.gl = gl;
        register(PositionColorProgram.DEFINITION);

        final VertexBufferObject vbo = new VertexBufferObject(OpenGL.BufferType.ARRAY, OpenGL.BufferUsage.STATIC_DRAW);
        renderNGon(MAX_VERTEX_COUNT,0,0,1,1);
        upload(vbo, vertexBuffer);

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
        gl.clearColor(color.r,color.g,color.b,color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void init() {
        // recompiling early used programs
        for (ShaderProgram program: programs.values()) {
            program.make(gl);
        }

        // generating
        for (VertexBufferObject vbo: vbos) {
            vbo.create(gl);
        }
    }

    public void register(VertexBufferObject vbo) {
        if (vbos.contains(vbo)) {
            return;
        }
        vbo.create(gl);
        vbos.add(vbo);
    }

    public void upload(VertexBufferObject vbo, VertexBuffer buffer) {
        vbo.upload(gl, buffer);
    }

    public void unregister(VertexBufferObject vbo) {
        if (vbos.remove(vbo)) {
            vbo.delete(gl);
        }
    }

    public Matrix getActiveMatrix() {
        return activeMatrix;
    }

    public class Binder {

        private ShaderProgram program;

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

        public Binder bindAttr(int index, VertexBuffer buffer, int item) {
            gl.vertexAttribPointer(index,
                    buffer.getCount(item), OpenGL.Type.FLOAT, false,
                    buffer.getStride(),
                    buffer.getBuffer(item));
            gl.enableVertexAttribArray(index);
            return this;
        }

        public Binder bindAttr(int index, VertexBufferObject vbo, int item) {
            vbo.bind(gl);
            gl.vertexAttribPointer(index,
                    vbo.getCount(item), OpenGL.Type.FLOAT, false,
                    vbo.getStride(), 0);
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

    public ShaderProgram register(ShaderProgram.Definition def) {
        ShaderProgram p = programs.get(def);
        if (p != null) {
            return p;
        }
        p = def.createProgram();
        programs.put(def, p);
        return p;
    }

    public void unregister(ShaderProgram.Definition def) {
        final ShaderProgram p = programs.get(def);
        if (p == null) {
            return;
        }
        p.delete(gl);
    }

    public Binder use(ShaderProgram.Definition def) {
        final ShaderProgram p = register(def);
        if (p == binder.program) {
            return binder;
        }
        p.make(gl);
        gl.useProgram(p.getId());
        binder.program = p;
        return binder;
    }

    private void drawBuf(OpenGL.PrimitiveType type, Color color, int count) {
        use(PositionColorProgram.DEFINITION).
                bindAttr(PositionColorProgram.POSITION_ATTR, vertexBuffer, 0).
                bindUniform(PositionColorProgram.COLOR_UNIFORM, color).
                bindUniform(PositionColorProgram.MATRIX_UNIFORM, activeMatrix).
                draw(type, count);
    }

    private int renderNGon(int n, float x, float y, float rx, float ry) {
        if (n < 3) {
            throw new IllegalArgumentException("N-Gon should have at least 3 points");
        }
        if (n > MAX_VERTEX_COUNT) {
            n = MAX_VERTEX_COUNT;
        }

        final float a = FloatMathUtils.TWO_PI/n;
        final float c = FloatMathUtils.cos(a), s = FloatMathUtils.sin(a);
        vertexBuffer.reset().layout(2);
        float vx = 1, vy = 0;
        for (int i=0; i<n; i++) {
            vertexBuffer.put(x + vx*rx, y + vy*ry);
            final float ny = vx*s+vy*c;
            vx = vx*c-vy*s;
            vy = ny;
        }
        return n;
    }

    public void fillNGon(int n, float x, float y, float rx, float ry, Color color) {
        n = renderNGon(n, x, y, rx, ry);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_FAN, color, n);
    }

    public void drawNGon(int n, float x, float y, float rx, float ry, Color color) {
        n = renderNGon(n, x, y, rx, ry);
        drawBuf(OpenGL.PrimitiveType.LINE_LOOP, color, n);
    }

    public void fillConvexPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset().layout(2).put(points, start, size);
        drawBuf(OpenGL.PrimitiveType.TRIANGLE_FAN, color, points.length / 2);
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

    public void fillEllipse(float x, float y, float rx, float ry, Color color) {
//        use(PositionColorProgram.DEFINITION).
//                bindAttr(PositionColorProgram.POSITION_ATTR, vbo, 0).
//                bindUniform(PositionColorProgram.COLOR_UNIFORM, color).
//                bindUniform(PositionColorProgram.MATRIX_UNIFORM, activeMatrix).
//                draw(OpenGL.PrimitiveType.TRIANGLE_FAN, vbo.size()/2);
    }

    public void drawEllipse(float x, float y, float rx, float ry, Color color) {
        //draw
    }

    public void setLineWidth(float width) {
        gl.lineWidth(width);
    }

}
