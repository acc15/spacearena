package ru.spacearena.engine.graphics;

import cern.colt.list.FloatArrayList;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.Rect2I;
import ru.spacearena.engine.graphics.font.CharData;
import ru.spacearena.engine.graphics.font.DistanceFieldProgram;
import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.FontIO;
import ru.spacearena.engine.graphics.shaders.PositionProgram;
import ru.spacearena.engine.graphics.shaders.Program;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureProgram;
import ru.spacearena.engine.graphics.vbo.VBODefinition;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.HashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext {

    public static final VBODefinition SIN_COS_VBO = new VBODefinition(
            OpenGL.ARRAY_BUFFER, OpenGL.STATIC_DRAW);

    public static final float DEFAULT_DENSITY_SCALE = 1f;
    public static final float DEFAULT_FONT_SCALE = 1f;
    public static final float DENSITY_SCALE_PPI = 160f;

    public enum Unit {
        DP,
        SP,
        PX
    }

    private final OpenGL gl;

    private final VertexBuffer vertexBuffer = new VertexBuffer();

    private final Matrix activeMatrix = new Matrix();
    private final FloatArrayList matrixStack = new FloatArrayList(Matrix.ELEMENTS_PER_MATRIX * 5);

    private final HashMap<Program.Definition, Program> programs =
            new HashMap<Program.Definition, Program>();
    private final HashMap<VertexBufferObject.Definition, VertexBufferObject> vbos =
            new HashMap<VertexBufferObject.Definition, VertexBufferObject>();
    private final HashMap<Texture.Definition, Texture> textures = new HashMap<Texture.Definition, Texture>();
    private final HashMap<FontData.Definition, FontData> fonts = new HashMap<FontData.Definition, FontData>();

    private final Binder binder = new Binder();

    private float densityScale = DEFAULT_DENSITY_SCALE;
    private float fontScale = DEFAULT_FONT_SCALE;

    public DrawContext(OpenGL gl) {
        this.gl = gl;
    }

    public void pushMatrix(Matrix m) {
        final int offset = matrixStack.size();
        matrixStack.setSize(offset + Matrix.ELEMENTS_PER_MATRIX);
        activeMatrix.toArrayCompact(matrixStack.elements(), offset);
        activeMatrix.postMultiply(m);
    }

    public void popMatrix() {
        if (matrixStack.isEmpty()) {
            throw new IllegalStateException("Empty matrix stack");
        }
        final int newSize = matrixStack.size() - Matrix.ELEMENTS_PER_MATRIX;
        activeMatrix.fromArrayCompact(matrixStack.elements(), newSize);
        matrixStack.setSize(newSize);
    }

    public void clear(Color color) {
        gl.clearColor(color.r, color.g, color.b, color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void init() {
        gl.enable(OpenGL.BLEND);
        gl.blendFunc(OpenGL.SRC_ALPHA, OpenGL.ONE_MINUS_SRC_ALPHA);
    }

    public void dispose() {
        programs.clear();
        vbos.clear();
        textures.clear();
    }

    public Matrix getActiveMatrix() {
        return activeMatrix;
    }

    public Program make(Program.Definition def) {
        Program p = programs.get(def);
        if (p != null) {
            return p;
        }
        p = def.createProgram();
        programs.put(def, p);
        p.make(gl);
        return p;
    }

    public void delete(Program.Definition definition) {
        final Program p = programs.get(definition);
        if (p == null) {
            return;
        }
        p.delete(gl);
        programs.remove(definition);
    }

    public Binder use(Program.Definition definition) {
        return binder.use(make(definition));
    }

    public boolean has(Program.Definition definition) {
        return programs.containsKey(definition);
    }

    public boolean has(VertexBufferObject.Definition definition) {
        return vbos.containsKey(definition);
    }

    public boolean has(Texture.Definition definition) { return textures.containsKey(definition); }

    public VertexBufferObject upload(VertexBufferObject.Definition definition, VertexBuffer buffer) {
        VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            vbo = new VertexBufferObject();
        }
        vbo.upload(gl, definition, buffer);
        vbos.put(definition, vbo);
        return vbo;
    }

    public void delete(VertexBufferObject.Definition definition) {
        final VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            throw new IllegalArgumentException("VBO with definition " + definition + " doesn't exists");
        }
        vbo.delete(gl);
        vbos.remove(definition);
    }

    public Texture load(Texture.Definition definition) {
        Texture t = textures.get(definition);
        if (t == null) {
            t = definition.createTexture(gl);
            textures.put(definition, t);
        }
        return t;
    }

    public FontData load(FontData.Definition definition) {
        FontData fontData = fonts.get(definition);
        if (fontData != null) {
            return fontData;
        }
        fontData = FontIO.load(definition.getFontUrl());
        load(definition.getTexture());
        fonts.put(definition, fontData);
        return fontData;
    }

    public void delete(FontData.Definition definition) {
        fonts.remove(definition);
        delete(definition.getTexture());
    }

    public void delete(Texture.Definition definition) {
        final Texture t = textures.get(definition);
        if (t == null) {
            throw new IllegalArgumentException("Texture with definition " + definition + " doesn't exists");
        }
        gl.deleteTexture(t.getId());
        textures.remove(definition);
    }

    public void drawImage(float x, float y, Texture.Definition texture) {
        final Texture t = load(texture);
        drawImage(x, y, x + t.getWidth(), y + t.getHeight(), texture);
    }

    public void drawImage(float l, float t, float r, float b, Texture.Definition definition) {
        final Texture texture = load(definition);
        vertexBuffer.reset(TextureProgram.LAYOUT_PT2).
                     put(l,t).put(texture.getLeft(), texture.getTop()).
                     put(l,b).put(texture.getLeft(), texture.getBottom()).
                     put(r,b).put(texture.getRight(), texture.getBottom()).
                     put(r,t).put(texture.getRight(), texture.getTop());
        use(TextureProgram.DEFINITION).
                bindAttr(TextureProgram.POSITION_ATTR, vertexBuffer, 0).
                bindAttr(TextureProgram.TEXCOORD_ATTR, vertexBuffer, 1).
                bindUniform(TextureProgram.MATRIX_UNIFORM, activeMatrix).
                bindUniform(TextureProgram.TEXTURE_UNIFORM, definition, 0).
                draw(OpenGL.TRIANGLE_FAN);
    }

    public void drawText(String text, float x, float y, FontData.Definition font, float size, Color color) {

        final FontData f = load(font);
        final Texture t = load(font.getTexture());

        final float scale = size/f.getFontSize();// * fontScale;
        final float lineHeight = f.getLineHeight() * scale;

        float currentX = x, currentY = y;

        vertexBuffer.reset(TextureProgram.LAYOUT_PT2);
        for (int i=0; i<text.length(); i++) {
            final char ch = text.charAt(i);
            switch (ch) {
            case '\n':
                currentY += lineHeight;
                currentX = x;
                break;

            case ' ':
                currentX += f.getSpaceAdvance() * scale;
                break;

            case '\t':
                currentX += f.getTabAdvance() * scale;
                break;

            default:

                final CharData ci = f.getCharInfo(ch);
                final Rect2I charRect = ci.getRect();

                final float offsetX = ci.getOffsetX() * scale;
                final float offsetY = ci.getOffsetY() * scale;
                final float height = charRect.getHeight() * scale;
                final float width = charRect.getWidth() * scale;
                final float advance = ci.getAdvance() * scale;

                final float tl = t.computeX((float) charRect.getLeft() / f.getImageWidth()),
                            tt = t.computeY((float) charRect.getTop() / f.getImageHeight()),
                            tr = t.computeX((float) charRect.getRight() / f.getImageWidth()),
                            tb = t.computeY((float) charRect.getBottom() / f.getImageHeight());

                final float ll = currentX + offsetX,
                            lt = currentY + offsetY,
                            lr = ll + width,
                            lb = lt + height;
                vertexBuffer.// first triangle
                        put(ll, lt).put(tl, tt).
                        put(ll, lb).put(tl, tb).
                        put(lr, lb).put(tr, tb).
                        // second triangle
                        put(ll, lt).put(tl, tt).
                        put(lr, lb).put(tr, tb).
                        put(lr, lt).put(tr, tt);
                currentX += advance;
                break;
            }
        }

        use(DistanceFieldProgram.DEFINITION).
                bindAttr(DistanceFieldProgram.POSITION_ATTR, vertexBuffer, 0).
                bindAttr(DistanceFieldProgram.TEXCOORD_ATTR, vertexBuffer, 1).
                bindUniform(DistanceFieldProgram.MATRIX_UNIFORM, activeMatrix).
                bindUniform(DistanceFieldProgram.TEXTURE_UNIFORM, font.getTexture(), 0).
                bindUniform(DistanceFieldProgram.COLOR_UNIFORM, color).
                bindUniform(DistanceFieldProgram.SMOOTH_UNIFORM, (float)(1<<f.getImageScale())/(size)).
                draw(OpenGL.TRIANGLES);
    }

    public void fillNGon(int n, float x, float y, float rx, float ry, Color color) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.TRIANGLE_FAN, color);
    }

    public void drawNGon(int n, float x, float y, float rx, float ry, Color color) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.LINE_LOOP, color);
    }

    public void fillConvexPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.TRIANGLE_FAN, color);
    }

    public void drawPoly(float[] points, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points);
        drawBuf(OpenGL.LINE_LOOP, color);
    }

    public void drawPoly(float[] points, int start, int size, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.LINE_LOOP, color);
    }

    public void fillRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.TRIANGLE_FAN, color);
    }

    public void drawRect(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.LINE_LOOP, color);
    }

    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x2, y2);
        drawBuf(OpenGL.LINES, color);
    }

    private void renderEllipse(int type, float x, float y, float rx, float ry, Color color) {
        ensureSinCosVBO();

        // storing current matrix on stack... looks very bad but should work
        final float m0 = activeMatrix.m[0], m1 = activeMatrix.m[1], m4 = activeMatrix.m[4], m5 = activeMatrix.m[5],
                    m12 = activeMatrix.m[12], m13 = activeMatrix.m[13];
        try {
            activeMatrix.postTranslate(x, y);
            activeMatrix.postScale(rx, ry);
            use(PositionProgram.DEFINITION).
                    bindAttr(PositionProgram.POSITION_ATTR, SIN_COS_VBO, 0).
                    bindUniform(PositionProgram.COLOR_UNIFORM, color).
                    bindUniform(PositionProgram.MATRIX_UNIFORM, activeMatrix).
                    draw(type);
        } finally {
            activeMatrix.set(m0, m1, m4, m5, m12, m13);
        }
    }

    private void ensureSinCosVBO() {
        if (has(SIN_COS_VBO)) {
            return;
        }
        fillNGonBuf(40, 0, 0, 1, 1);
        upload(SIN_COS_VBO, vertexBuffer);
    }

    public void fillEllipse(float x, float y, float rx, float ry, Color color) {
        renderEllipse(OpenGL.TRIANGLE_FAN, x, y, rx, ry, color);
    }

    public void drawEllipse(float x, float y, float rx, float ry, Color color) {
        renderEllipse(OpenGL.LINE_LOOP, x, y, rx, ry, color);
    }

    public void fillCircle(float x, float y, float r, Color color) {
        renderEllipse(OpenGL.TRIANGLE_FAN, x, y, r, r, color);
    }

    public void drawCircle(float x, float y, float r, Color color) {
        renderEllipse(OpenGL.LINE_LOOP, x, y, r, r, color);
    }

    public void setLineWidth(float width) {
        gl.lineWidth(width);
    }

    public float getDensityScale() {
        return densityScale;
    }

    public void setDensityScale(float densityScale) {
        this.fontScale *= densityScale / this.densityScale;
        this.densityScale = densityScale;
    }

    public void setDensityScale(float densityScale, float fontScale) {
        this.densityScale = densityScale;
        this.fontScale = fontScale;
    }

    public float getFontScale() {
        return fontScale;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = this.densityScale * fontScale;
    }

    public class Binder {

        private Program program;
        private int vertexCount = -1;
        private boolean texturing = false;

        public Binder bindUniform(int index, float x) {
            gl.uniform(program.getUniformLocation(index), x);
            return this;
        }

        public Binder bindUniform(int index, float x, float y) {
            gl.uniform(program.getUniformLocation(index), x, y);
            return this;
        }

        public Binder bindUniform(int index, float x, float y, float z) {
            gl.uniform(program.getUniformLocation(index), x, y, z);
            return this;
        }

        public Binder bindUniform(int index, float x, float y, float z, float w) {
            gl.uniform(program.getUniformLocation(index), x, y, z, w);
            return this;
        }

        public Binder bindUniform(int index, Texture.Definition def, int unit) {
            final Texture t = load(def);
            if (!texturing) {
                texturing = true;
                gl.enable(OpenGL.TEXTURE_2D);
            }
            gl.activeTexture(OpenGL.TEXTURE0 + unit);
            gl.bindTexture(OpenGL.TEXTURE_2D, t.getId());

            final int loc = program.getUniformLocation(index);
            gl.uniform(loc, unit);
            return this;
        }

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

        public Binder bindAttr(int index, VertexBufferObject.Definition definition, int item) {
            final VertexBufferObject vbo = vbos.get(definition);
            if (vbo == null) {
                throw new IllegalArgumentException("VBO with definition " + definition + " doesn't exists in current context");
            }

            final VertexBufferLayout vbl = vbo.getLayout();
            final int size = vbo.getSize(),
                      stride = vbl.getStride(),
                      type = vbl.getType(item),
                      count = VertexBufferLayout.toTypes(vbl.getSize(item), type),
                      offset = vbl.getOffset(item);

            final int bufferType = definition.getBufferType();
            gl.bindBuffer(bufferType, vbo.getId());
            gl.vertexAttribPointer(index, count, type, false, stride, offset);
            gl.enableVertexAttribArray(index);
            gl.bindBuffer(bufferType, 0);
            adjustVertexCount(size, stride);
            return this;
        }

        public Binder bindAttr(int index, VertexBuffer buffer, int item) {
            final VertexBufferLayout vbl = buffer.getLayout();
            final int size = buffer.getSize(),
                      stride = vbl.getStride(),
                      type = vbl.getType(item),
                      count = VertexBufferLayout.toTypes(vbl.getSize(item), type);
            gl.vertexAttribPointer(index, count, type, false, stride, buffer.prepareBuffer(item));
            gl.enableVertexAttribArray(index);
            adjustVertexCount(size, stride);
            return this;
        }

        public void draw(int type) {
            draw(type, 0, vertexCount);
        }

        public void draw(int type, int count) {
            draw(type, 0, count);
        }

        public void draw(int type, int start, int count) {
            gl.drawArrays(type, start, count);
            if (texturing) {
                gl.disable(OpenGL.TEXTURE_2D);
                texturing = false;
            }
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
            gl.useProgram(program.getId());
            this.program = program;
            return binder;
        }
    }

    private void drawBuf(int type, Color color) {
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
        n = Math.min(n,360);

        final float a = FloatMathUtils.TWO_PI / n;
        final float c = FloatMathUtils.cos(a), s = FloatMathUtils.sin(a);
        vertexBuffer.reset(PositionProgram.LAYOUT_P2);
        float vx = 1, vy = 0;
        for (int i = 0; i < n; i++) {
            vertexBuffer.put(x + vx * rx, y + vy * ry);
            final float ny = vx * s + vy * c;
            vx = vx * c - vy * s;
            vy = ny;
        }
    }

}
