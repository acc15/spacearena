package ru.spacearena.engine.graphics;

import cern.colt.list.FloatArrayList;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.font.CharData;
import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.FontRepository;
import ru.spacearena.engine.graphics.shaders.DefaultShaders;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class DrawContext2f extends GLDrawContext {

    public static final VertexBufferObject.Definition SIN_COS_VBO = new VertexBufferObject.Definition(
            OpenGL.GL_ARRAY_BUFFER, OpenGL.GL_STATIC_DRAW) {

        private final int NPOINTS = 100;

        private final Point2F pt = new Point2F(1,0);

        @Override
        public void init(GLDrawContext context, VertexBufferObject vbo) {
            final float a = FloatMathUtils.TWO_PI / NPOINTS;
            final float c = FloatMathUtils.cos(a), s = FloatMathUtils.sin(a);
            final VertexBuffer buf = context.getSharedBuffer();
            buf.reset(DefaultShaders.LAYOUT_P2);
            for (int i = 0; i < NPOINTS; i++) {
                buf.put(pt.x, pt.y);
                pt.rotate(c,s);
            }
            vbo.upload(context.getGL(), this, buf);
        }
    };

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

    private final Matrix activeMatrix = new Matrix();
    private final FloatArrayList matrixStack = new FloatArrayList(Matrix.ELEMENTS_PER_MATRIX * 5);

    private final Polygon polygon = new Polygon();

    private float densityScale = 1f;
    private float fontScale = 1f;

    private FontData.Definition font = FontRepository.CALIBRI;
    private float fontSize = 16;
    private Color color = Color.BLACK;

    public DrawContext2f(OpenGL gl) {
        super(gl);
    }

    @Override
    public void init() {
        gl.glEnable(OpenGL.GL_VERTEX_PROGRAM_POINT_SIZE);
        gl.glEnable(OpenGL.GL_BLEND);
        gl.glBlendFunc(OpenGL.GL_SRC_ALPHA, OpenGL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(OpenGL.GL_DEPTH_TEST);
        gl.glDepthMask(false);
    }

    public void pushMatrix() {
        final int offset = matrixStack.size();
        matrixStack.setSize(offset + Matrix.ELEMENTS_PER_MATRIX);
        activeMatrix.toArrayCompact(matrixStack.elements(), offset);
    }

    public void setMatrix(Matrix m) {
        pushMatrix();
        activeMatrix.set(m);
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
        gl.glClearColor(color.r, color.g, color.b, color.a);
        gl.glClear(OpenGL.GL_COLOR_BUFFER_BIT);
    }

    public Matrix getActiveMatrix() {
        return activeMatrix;
    }

    public void drawImage(float x, float y, Texture.Definition texture) {
        final Texture t = obtain(texture);
        drawImage(x, y, x + t.getWidth(), y + t.getHeight(), texture);
    }

    public void drawImage(float l, float t, float r, float b, Texture.Definition definition) {
        final Texture texture = obtain(definition);
        getSharedBuffer().reset(DefaultShaders.LAYOUT_P2T2).
                put(l, t).put(texture.getLeft(), texture.getTop()).
                put(l, b).put(texture.getLeft(), texture.getBottom()).
                put(r, b).put(texture.getRight(), texture.getBottom()).
                put(r, t).put(texture.getRight(), texture.getTop());
        use(DefaultShaders.POSITION_TEXTURE_PROGRAM).
                attrs(sharedBuffer).
                uniform(activeMatrix).
                uniform(definition, 0).
                draw(OpenGL.GL_TRIANGLE_FAN);
    }


    public float getFontLineHeight() {
        final FontData fd = obtain(font);
        return fd.getLineHeight() * fontSize / fd.getFontSize();
    }

    public void drawText(String text, float x, float y) {

        final FontData font = obtain(this.font);
        final Texture texture = obtain(this.font.getTexture());

        final float scale = fontSize / font.getFontSize();
        final float lineHeight = font.getLineHeight() * scale;

        float currentX = x, currentY = y;

        sharedBuffer.reset(DefaultShaders.LAYOUT_P2T2);

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
                    currentX += font.getSpaceAdvance() * scale;
                    break;

                case '\t':
                    currentX += font.getTabAdvance() * scale;
                    break;

                default:

                    final CharData ci = font.getCharInfo(ch);
                    final float offsetX = ci.getOffsetX() * scale;
                    final float offsetY = ci.getOffsetY() * scale;
                    final float height = ci.getHeight() * scale;
                    final float width = ci.getWidth() * scale;
                    final float advance = ci.getAdvance() * scale;

                    final float l = ci.getX(), t = ci.getY(), r = l + ci.getWidth(), b = t + ci.getHeight();
                    final float tl = texture.computeX(l / font.getImageWidth()),
                                tt = texture.computeY(t / font.getImageHeight()),
                                tr = texture.computeX(r / font.getImageWidth()),
                                tb = texture.computeY(b / font.getImageHeight());

                    final float ll = currentX + offsetX,
                                lt = currentY + offsetY,
                                lr = ll + width,
                                lb = lt + height;
                    sharedBuffer.// first triangle
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

        use(DefaultShaders.DISTANCE_FIELD_PROGRAM).
                attrs(sharedBuffer).
                uniform(activeMatrix).
                uniform(this.font.getTexture(), 0).
                uniform(color).
                uniform((float) (1 << font.getImageScale()) / fontSize).
                draw(OpenGL.GL_TRIANGLES);
    }

    public void fillNGon(int n, float x, float y, float rx, float ry) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.GL_TRIANGLE_FAN);
    }

    public void drawNGon(int n, float x, float y, float rx, float ry) {
        fillNGonBuf(n, x, y, rx, ry);
        drawBuf(OpenGL.GL_LINE_LOOP);
    }

    public void fillConvexPoly(float[] points, int start, int size) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.GL_TRIANGLE_FAN);
    }

    public Polygon polygon() {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2);
        return polygon;
    }

    public void drawPoly(float[] points) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(points);
        drawBuf(OpenGL.GL_LINE_LOOP);
    }

    public void drawPoly(float[] points, int start, int size) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(points, start, size);
        drawBuf(OpenGL.GL_LINE_LOOP);
    }

    public void fillRect(float x1, float y1, float x2, float y2) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.GL_TRIANGLE_FAN);
    }

    public void drawRect(float x1, float y1, float x2, float y2) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(x1, y1).put(x1, y2).put(x2, y2).put(x2, y1);
        drawBuf(OpenGL.GL_LINE_LOOP);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2).put(x1, y1).put(x2, y2);
        drawBuf(OpenGL.GL_LINES);
    }

    public void drawArrow(float x1, float y1, float x2, float y2, float size) {

        final float vx = x2 - x1, vy = y2 - y1;
        if (FloatMathUtils.isZero(vx, vy)) {
            return;
        }

        final float l = FloatMathUtils.length(vx, vy);
        final float nx = vx/l, ny = vy/l;
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2);
        sharedBuffer.put(x1, y1).put(x2, y2);
        sharedBuffer.put(x2, y2, x2 - (nx * COS_30 - ny * SIN_30) * size, y2 - (ny * COS_30 + nx * SIN_30) * size);
        sharedBuffer.put(x2, y2, x2 - (nx * COS_30 + ny * SIN_30) * size, y2 - (ny * COS_30 - nx * SIN_30) * size);
        drawBuf(OpenGL.GL_LINES);
    }

    private void renderEllipse(int type, float x, float y, float rx, float ry) {
        pushMatrix();
        try {
            activeMatrix.postTranslate(x, y);
            activeMatrix.postScale(rx, ry);
            use(DefaultShaders.POSITION_PROGRAM).
                    attrs(SIN_COS_VBO).
                    uniform(activeMatrix).
                    uniform(color).
                    draw(type);
        } finally {
            popMatrix();
        }
    }

    public void fillEllipse(float x, float y, float rx, float ry) {
        renderEllipse(OpenGL.GL_TRIANGLE_FAN, x, y, rx, ry);
    }

    public void drawEllipse(float x, float y, float rx, float ry) {
        renderEllipse(OpenGL.GL_LINE_LOOP, x, y, rx, ry);
    }

    public void fillCircle(float x, float y, float r) {
        renderEllipse(OpenGL.GL_TRIANGLE_FAN, x, y, r, r);
    }

    public void drawCircle(float x, float y, float r) {
        renderEllipse(OpenGL.GL_LINE_LOOP, x, y, r, r);
    }

    public FontData getFont() {
        return obtain(font);
    }

    public DrawContext2f font(FontData.Definition definition) {
        this.font = definition;
        return this;
    }

    public DrawContext2f densityScale(float densityScale, float fontScale) {
        this.densityScale = densityScale;
        this.fontScale = fontScale;
        return this;
    }

    public DrawContext2f densityScale(float densityScale) {
        this.fontScale *= densityScale / this.densityScale;
        this.densityScale = densityScale;
        return this;
    }

    public DrawContext2f fontScale(float fontScale) {
        this.fontScale = this.densityScale * fontScale;
        return this;
    }

    public DrawContext2f fontSize(float fontSize) {
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

    public DrawContext2f fontSize(float fontSize, Unit unit) {
        this.fontSize = convert(fontSize, unit, Unit.PX);
        return this;
    }

    public DrawContext2f lineWidth(float width) {
        getGL().glLineWidth(width);
        return this;
    }

    public DrawContext2f color(Color color) {
        this.color = color;
        return this;
    }

    public class Polygon {
        public Polygon put(float x, float y) {
            sharedBuffer.put(x, y);
            return this;
        }

        public Polygon put(float[] points, int start, int length) {
            sharedBuffer.put(points, start, length);
            return this;
        }

        public Polygon put(float[] points) {
            sharedBuffer.put(points);
            return this;
        }

        public void draw() {
            drawBuf(OpenGL.GL_LINE_LOOP);
        }

        public void fill() {
            drawBuf(OpenGL.GL_TRIANGLE_FAN);
        }
    }

    private void drawBuf(int type) {
        use(DefaultShaders.POSITION_PROGRAM).
                attrs(sharedBuffer).
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
        sharedBuffer.reset(DefaultShaders.LAYOUT_P2);
        float vx = 1, vy = 0;
        for (int i = 0; i < n; i++) {
            sharedBuffer.put(x + vx * rx, y + vy * ry);
            final float ny = vx * s + vy * c;
            vx = vx * c - vy * s;
            vy = ny;
        }
    }

}
