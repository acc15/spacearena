package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.viewport.Viewport;
import ru.spacearena.engine.geometry.shapes.Rect2FR;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.random.QRand;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Sky extends EngineObject {

    private float starDistance = 25f;
    private float minStarSize = 1f;
    private float maxStarSize = 3f;

    private final QRand random = new QRand();
    private final int seed = random.nextInt();
    private final Rect2FR bounds = new Rect2FR();
    private final Viewport viewport;

    private final VertexBuffer vb = new VertexBuffer();

    public Sky(Viewport viewport) {
        this.viewport = viewport;
    }

    public float getStarDistance() {
        return starDistance;
    }

    public void setStarDistance(float starDistance) {
        this.starDistance = starDistance;
    }

    public float getMinStarSize() {
        return minStarSize;
    }

    public void setMinStarSize(float minStarSize) {
        this.minStarSize = minStarSize;
    }

    public float getMaxStarSize() {
        return maxStarSize;
    }

    public void setMaxStarSize(float maxStarSize) {
        this.maxStarSize = maxStarSize;
    }

    private void drawStarLayer(DrawContext context, float scale) {

        final float sd2 = starDistance*2;
        bounds.set(viewport.getRect());
        bounds.halfSize.add(sd2);
        bounds.halfSize.div(scale);

        final int x1 = (int)FloatMathUtils.ceil(bounds.getMinX()/starDistance),
                  x2 = (int)FloatMathUtils.floor(bounds.getMaxX()/starDistance),
                  y1 = (int)FloatMathUtils.ceil(bounds.getMinY()/starDistance),
                  y2 = (int)FloatMathUtils.floor(bounds.getMaxY()/starDistance);

        for (int i = y1; i<=y2; i++) {
            for (int j = x1; j<=x2; j++) {
                random.setSeed(seed ^
                        Integer.rotateLeft(Float.floatToRawIntBits(scale), 16) ^
                        Integer.rotateLeft(i, 8) ^ j);

                final float dx = random.nextFloatBetween(-sd2, sd2), dy = random.nextFloatBetween(-sd2, sd2);
                final float x = j * starDistance, y = i * starDistance;
                final float gx = (x-bounds.position.x)*scale+bounds.position.x,
                            gy = (y-bounds.position.y)*scale+bounds.position.y;
                final float sx = gx + dx*scale, sy = gy + dy*scale;
                final float bright = random.nextFloat();
                final float size = FloatMathUtils.clamp(random.nextFloat() * 4 * context.getDensityScale(), 1, 4);
                vb.put(sx, sy).put(bright, bright, 1).put(size);
            }
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        vb.reset(LAYOUT_P2C3S1);
        for (float scale=5; scale>0; scale--) {
            drawStarLayer(context, 1-scale/10);
        }
        context.use(SHADER).
                attrs(vb).
                uniform(context.getActiveMatrix()).
                draw(OpenGL.POINTS);
    }

    public static final ShaderProgram.Definition SHADER = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            final ShaderProgram p = new ShaderProgram();
            p.shader(Sky.class, "sky.vert");
            p.shader(Sky.class, "sky.frag");
            p.attribute("a_Position");
            p.attribute("a_Color");
            p.attribute("a_PointSize");
            p.uniform("u_MVPMatrix");
            return p;
        }
    };

    public static final VertexBufferLayout LAYOUT_P2C3S1 = VertexBufferLayout.create().
            floats(2).floats(3).floats(1).build();


}