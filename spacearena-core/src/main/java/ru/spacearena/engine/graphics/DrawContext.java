package ru.spacearena.engine.graphics;

import cern.colt.list.FloatArrayList;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.Rect2I;
import ru.spacearena.engine.graphics.font.*;
import ru.spacearena.engine.graphics.shaders.PositionProgram;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.shaders.TextureProgram;
import ru.spacearena.engine.graphics.vbo.VBODefinition;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.IntMathUtils;

import java.util.HashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext {

    public static final VBODefinition SIN_COS_VBO = new VBODefinition(
            OpenGL.ARRAY_BUFFER, OpenGL.STATIC_DRAW);

    public static final float DENSITY_SCALE_PPI = 160f;
    public static final float DEFAULT_DENSITY_SCALE = 96f/DENSITY_SCALE_PPI;
    public static final float DEFAULT_FONT_SCALE = 1f;//96f/DENSITY_SCALE_PPI; // by default 96 DPI is used
    public static final float INCH_PER_MM = 0.0393700787f;

    private static final float SIN_30 = 0.5f;
    private static final float COS_30 = 0.86602540378f;

    public float getDensityScale() {
        return densityScale;
    }


    public enum Unit {
        DP,
        SP,
        PX
    }

    private final OpenGL gl;

    private final VertexBuffer vertexBuffer = new VertexBuffer();

    private final Matrix activeMatrix = new Matrix();
    private final FloatArrayList matrixStack = new FloatArrayList(Matrix.ELEMENTS_PER_MATRIX * 5);

    private final HashMap<ShaderProgram.Definition, ShaderProgram> programs = new HashMap<ShaderProgram.Definition, ShaderProgram>();
    private final HashMap<VertexBufferObject.Definition, VertexBufferObject> vbos = new HashMap<VertexBufferObject.Definition, VertexBufferObject>();
    private final HashMap<Texture.Definition, Texture> textures = new HashMap<Texture.Definition, Texture>();
    private final HashMap<FontData.Definition, FontData> fonts = new HashMap<FontData.Definition, FontData>();

    private final Binder binder = new Binder();
    private final Polygon polygon = new Polygon();

    private float densityScale = 1f;
    private float fontScale = 1f;

    private FontData.Definition font = FontRepository.CALIBRI;
    private float fontSize = 16;
    private Color color = Color.BLACK;

    public DrawContext(OpenGL gl) {
        this.gl = gl;
    }

    public void pushMatrix() {
        final int offset = matrixStack.size();
        matrixStack.setSize(offset + Matrix.ELEMENTS_PER_MATRIX);
        activeMatrix.toArrayCompact(matrixStack.elements(), offset);
    }

    public void multiplyMatrix(Matrix m) {
        pushMatrix();
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

    public void clear() {
        gl.clearColor(color.r, color.g, color.b, color.a);
        gl.clear(OpenGL.COLOR_BUFFER_BIT);
    }

    public void init() {
        gl.enable(OpenGL.VERTEX_PROGRAM_POINT_SIZE);
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

    public Binder use(ShaderProgram.Definition definition) {
        return binder.use(get(definition));
    }

    public boolean has(ShaderProgram.Definition definition) {
        return programs.containsKey(definition);
    }

    public boolean has(VertexBufferObject.Definition definition) {
        return vbos.containsKey(definition);
    }

    public boolean has(Texture.Definition definition) {
        return textures.containsKey(definition);
    }

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
        final VertexBufferObject vbo = get(definition);
        vbo.delete(gl);
        vbos.remove(definition);
    }

    public VertexBufferObject get(VertexBufferObject.Definition definition) {
        final VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            throw new IllegalArgumentException("VBO with definition " + definition + " doesn't exists");
        }
        return vbo;
    }

    public ShaderProgram get(ShaderProgram.Definition def) {
        ShaderProgram p = programs.get(def);
        if (p != null) {
            return p;
        }
        p = def.createProgram();
        programs.put(def, p);
        p.make(gl);
        return p;
    }

    public Texture get(Texture.Definition definition) {
        Texture t = textures.get(definition);
        if (t == null) {
            t = definition.createTexture(gl);
            textures.put(definition, t);
        }
        return t;
    }

    public FontData get(FontData.Definition definition) {
        FontData fontData = fonts.get(definition);
        if (fontData != null) {
            return fontData;
        }
        fontData = FontIO.load(definition.getFontUrl());
        get(definition.getTexture());
        fonts.put(definition, fontData);
        return fontData;
    }

    public void delete(ShaderProgram.Definition definition) {
        final ShaderProgram p = programs.get(definition);
        if (p == null) {
            return;
        }
        p.delete(gl);
        programs.remove(definition);
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
        final Texture t = get(texture);
        drawImage(x, y, x + t.getWidth(), y + t.getHeight(), texture);
    }

    public void drawImage(float l, float t, float r, float b, Texture.Definition definition) {
        final Texture texture = get(definition);
        vertexBuffer.reset(TextureProgram.LAYOUT_PT2).
                put(l, t).put(texture.getLeft(), texture.getTop()).
                put(l, b).put(texture.getLeft(), texture.getBottom()).
                put(r, b).put(texture.getRight(), texture.getBottom()).
                put(r, t).put(texture.getRight(), texture.getTop());
        use(TextureProgram.DEFINITION).
                attrs(vertexBuffer).
                uniform(activeMatrix).
                uniform(definition, 0).
                draw(OpenGL.TRIANGLE_FAN);
    }


    public float getFontLineHeight() {
        final FontData fd = get(font);
        return fd.getLineHeight() * fontSize / fd.getFontSize();
    }

    public void drawText(String text, float x, float y) {

        final FontData f = get(font);
        final Texture t = get(font.getTexture());

        final float scale = fontSize / f.getFontSize();
        final float lineHeight = f.getLineHeight() * scale;

        float currentX = x, currentY = y;

        vertexBuffer.reset(TextureProgram.LAYOUT_PT2);

        boolean skipLF = false;
        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            // to support all possible line end combinations: CR (Mac OS), LF (Unix), CR LF (Windows)
            if (skipLF && ch == '\n') {
                skipLF = false;
                continue;
            }
            switch (ch) {
                case '\r':
                    skipLF = true;
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
                attrs(vertexBuffer).
                uniform(activeMatrix).
                uniform(font.getTexture(), 0).
                uniform(color).
                uniform((float) (1 << f.getImageScale()) / fontSize).
                draw(OpenGL.TRIANGLES);
    }

    public void fillNGon(int n, float x, float y, float rx, float ry) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.TRIANGLE_FAN);
    }

    public void drawNGon(int n, float x, float y, float rx, float ry) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.LINE_LOOP);
    }

    public void fillConvexPoly(float[] points, int start, int size) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.TRIANGLE_FAN);
    }

    public VertexBuffer getSharedBuffer() {
        return vertexBuffer;
    }

    public Polygon polygon() {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2);
        return polygon;
    }

    public void drawPoly(float[] points) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points);
        drawBuf(OpenGL.LINE_LOOP);
    }

    public void drawPoly(float[] points, int start, int size) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.LINE_LOOP);
    }

    public void fillRect(float x1, float y1, float x2, float y2) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.TRIANGLE_FAN);
    }

    public void drawRect(float x1, float y1, float x2, float y2) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.LINE_LOOP);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        vertexBuffer.reset(PositionProgram.LAYOUT_P2).put(x1, y1).put(x2, y2);
        drawBuf(OpenGL.LINES);
    }

    public void drawArrow(float x1, float y1, float x2, float y2, float size) {

        final float vx = x2 - x1, vy = y2 - y1;
        if (FloatMathUtils.isZero(vx, vy)) {
            return;
        }

        final float l = FloatMathUtils.length(vx, vy);
        final float nx = vx/l, ny = vy/l;
        vertexBuffer.reset(PositionProgram.LAYOUT_P2);
        vertexBuffer.put(x1, y1).put(x2, y2);
        vertexBuffer.put(x2, y2, x2 - (nx * COS_30 - ny * SIN_30) * size, y2 - (ny * COS_30 + nx * SIN_30) * size);
        vertexBuffer.put(x2, y2, x2 - (nx * COS_30 + ny * SIN_30) * size, y2 - (ny * COS_30 - nx * SIN_30) * size);
        drawBuf(OpenGL.LINES);
//        drawLine(x1, y1, x2, y2);
//
//        float p30x = 0f, p30y = 0f, s30x = 0f, s30y = 0f;
//        if (head == HeadType.ARROW || tail == HeadType.ARROW) {
//            final float vx = x2 - x1, vy = y2 - y1;
//            final float l = FloatMathUtils.length(vx, vy);
//            final float nx = vx/l, ny = vy/l;
//            p30x = nx * COS_30 - ny * SIN_30;
//            p30y = ny * COS_30 + nx * SIN_30;
//            s30x = nx * COS_30 + ny * SIN_30;
//            s30y = ny * COS_30 - nx * SIN_30;
//        }
//
//        switch (head) {
//            case CIRCLE:
//                context.fillCircle(x1, y1, headSize);
//                break;
//
//            case ARROW:
//                context.drawLine(x1, y1, x1 + p30x * headSize, y1 + p30y * headSize);
//                context.drawLine(x1, y1, x1 + s30x * headSize, y1 + s30y * headSize);
//                break;
//        }
//
//        switch (tail) {
//            case CIRCLE:
//                context.fillCircle(x2, y2, headSize);
//                break;
//
//            case ARROW:
//                context.drawLine(x2, y2, x2 - p30x * tailSize, y2 - p30y * tailSize);
//                context.drawLine(x2, y2, x2 - s30x * tailSize, y2 - s30y * tailSize);
//                break;
//        }
    }

    private void renderEllipse(int type, float x, float y, float rx, float ry) {
        ensureSinCosVBO();
        pushMatrix();
        try {
            activeMatrix.postTranslate(x, y);
            activeMatrix.postScale(rx, ry);
            use(PositionProgram.DEFINITION).
                    attrs(SIN_COS_VBO).
                    uniform(activeMatrix).
                    uniform(color).
                    draw(type);
        } finally {
            popMatrix();
        }
    }

    private void ensureSinCosVBO() {
        if (has(SIN_COS_VBO)) {
            return;
        }
        fillNGonBuf(40, 0, 0, 1, 1);
        upload(SIN_COS_VBO, vertexBuffer);
    }

    public void fillEllipse(float x, float y, float rx, float ry) {
        renderEllipse(OpenGL.TRIANGLE_FAN, x, y, rx, ry);
    }

    public void drawEllipse(float x, float y, float rx, float ry) {
        renderEllipse(OpenGL.LINE_LOOP, x, y, rx, ry);
    }

    public void fillCircle(float x, float y, float r) {
        renderEllipse(OpenGL.TRIANGLE_FAN, x, y, r, r);
    }

    public void drawCircle(float x, float y, float r) {
        renderEllipse(OpenGL.LINE_LOOP, x, y, r, r);
    }

    public FontData getFont() {
        return get(font);
    }

    public DrawContext font(FontData.Definition definition) {
        this.font = definition;
        return this;
    }

    public DrawContext densityScale(float densityScale, float fontScale) {
        this.densityScale = densityScale;
        this.fontScale = fontScale;
        return this;
    }

    public DrawContext densityScale(float densityScale) {
        this.fontScale *= densityScale / this.densityScale;
        this.densityScale = densityScale;
        return this;
    }

    public DrawContext fontScale(float fontScale) {
        this.fontScale = this.densityScale * fontScale;
        return this;
    }

    public DrawContext fontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public float convert(float value, Unit fromUnit, Unit toUnit) {
        switch (fromUnit) {
            case DP: value *= densityScale; break;
            case SP: value *= fontScale; break;
        }
        switch (toUnit) {
            case DP: value /= densityScale; break;
            case SP: value /= fontScale; break;
        }
        return value;
    }

    public DrawContext fontSize(float fontSize, Unit unit) {
        this.fontSize = convert(fontSize, unit, Unit.PX);
        return this;
    }

    public DrawContext lineWidth(float width) {
        gl.lineWidth(width);
        return this;
    }

    public DrawContext color(Color color) {
        this.color = color;
        return this;
    }

    public class Polygon {
        public Polygon put(float x, float y) {
            vertexBuffer.put(x,y);
            return this;
        }

        public Polygon put(float[] points, int start, int length) {
            vertexBuffer.put(points, start, length);
            return this;
        }

        public Polygon put(float[] points) {
            vertexBuffer.put(points);
            return this;
        }

        public void draw() {
            drawBuf(OpenGL.LINE_LOOP);
        }

        public void fill() {
            drawBuf(OpenGL.TRIANGLE_FAN);
        }
    }

    public class Binder {

        private ShaderProgram program;
        private int vertexCount = -1;
        private int attrIndex = -1;
        private int uniformIndex = -1;
        private boolean texturing = false;

        private int nextUniformLocation() {
            return program.getUniformLocation(++uniformIndex);
        }

        private int nextAttrIndex() {
            return ++attrIndex;
        }

        public Binder uniform(float x) {
            gl.uniform(nextUniformLocation(), x);
            return this;
        }

        public Binder uniform(float x, float y) {
            gl.uniform(nextUniformLocation(), x, y);
            return this;
        }

        public Binder uniform(float x, float y, float z) {
            gl.uniform(nextUniformLocation(), x, y, z);
            return this;
        }

        public Binder uniform(float x, float y, float z, float w) {
            gl.uniform(nextUniformLocation(), x, y, z, w);
            return this;
        }

        public Binder uniform(Texture.Definition def, int unit) {
            final Texture t = get(def);
            if (!texturing) {
                texturing = true;
                gl.enable(OpenGL.TEXTURE_2D);
            }
            gl.activeTexture(OpenGL.TEXTURE0 + unit);
            gl.bindTexture(OpenGL.TEXTURE_2D, t.getId());

            gl.uniform(nextUniformLocation(), unit);
            return this;
        }

        public Binder uniform(Point2F point) {
            gl.uniform(nextUniformLocation(), point.x, point.y);
            return this;
        }

        public Binder uniform(Matrix matrix) {
            gl.uniformMatrix4(nextUniformLocation(), 1, matrix.m, 0);
            return this;
        }

        public Binder uniform(Color color, boolean useAlpha) {
            if (useAlpha) {
                return uniform(color);
            }
            gl.uniform(nextUniformLocation(), color.r, color.g, color.b);
            return this;
        }

        public Binder uniform(Color color) {
            gl.uniform(nextUniformLocation(), color.r, color.g, color.b, color.a);
            return this;
        }

        private void attrPointerBuffer(int attrIndex, int item, VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            gl.vertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vb.prepareBuffer(item));
            gl.enableVertexAttribArray(attrIndex);
        }

        private void attrPointerOffset(int attrIndex, int item, VertexBufferLayout vbl) {
            gl.vertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vbl.getOffset(item));
            gl.enableVertexAttribArray(attrIndex);
        }

        public Binder attr(VertexBufferObject.Definition definition, int item) {
            final VertexBufferObject vbo = get(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.bindBuffer(definition.getBufferType(), vbo.getId());
            attrPointerOffset(nextAttrIndex(), item, vbl);
            gl.bindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
            return this;
        }

        public Binder attrs(VertexBufferObject.Definition definition) {
            final VertexBufferObject vbo = get(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.bindBuffer(definition.getBufferType(), vbo.getId());
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerOffset(nextAttrIndex(), i, vbl);
            }
            gl.bindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
            return this;
        }

        public Binder attr(VertexBuffer buffer, int item) {
            final VertexBufferLayout vbl = buffer.getLayout();
            attrPointerBuffer(nextAttrIndex(), item, buffer);
            adjustVertexCount(buffer.getSize(), vbl.getStride());
            return this;
        }

        public Binder attrs(VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerBuffer(nextAttrIndex(), i, vb);
            }
            adjustVertexCount(vb.getSize(), vbl.getStride());
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
            vertexCount = (vertexCount < 0 ? count : IntMathUtils.min(vertexCount, count));
        }

        public Binder use(ShaderProgram program) {
            this.vertexCount = -1;
            this.attrIndex = -1;
            this.uniformIndex = -1;
            if (this.program == program) {
                return binder;
            }
            gl.useProgram(program.getId());
            this.program = program;
            return binder;
        }


    }

    private void drawBuf(int type) {
        use(PositionProgram.DEFINITION).
                attrs(vertexBuffer).
                uniform(activeMatrix).
                uniform(color).
                draw(type);
    }

    private void fillNGonBuf(int n, float x, float y, float rx, float ry) {
        if (n < 3) {
            throw new IllegalArgumentException("N-Gon should have at least 3 points");
        }
        n = Math.min(n, 360);

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
