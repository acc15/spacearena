package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2ID;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.Texture;
import ru.spacearena.engine.graphics.fbo.FrameBufferObject;
import ru.spacearena.engine.graphics.shaders.DefaultShaders;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.random.QRand;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-27-03
 */
public class Explosion extends EngineObject {

    public static final float PARTICLE_SIZE = 0.4f; // particle size in meters

    public static final float DURATION = 1f;
    public static final float RADIUS = 2f;

    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 64;

    public static final Texture.Definition TEXTURE = new Texture.Definition().
            wrapS(OpenGL.GL_CLAMP_TO_EDGE).
            wrapT(OpenGL.GL_CLAMP_TO_EDGE).
            empty(TEXTURE_WIDTH, TEXTURE_HEIGHT, OpenGL.GL_RGBA, OpenGL.GL_UNSIGNED_BYTE);
    public static final FrameBufferObject.Definition FRAMEBUF = new FrameBufferObject.Definition().attach(TEXTURE);
    public static final Color INVISIBLE_BLACK = new Color(0,0,0,0);
    public static final Rect2ID RECT = new Rect2ID();
    public static final ShaderProgram.Definition BLUR_PROGRAM = new ShaderProgram.Definition().
        shader(DefaultShaders.class, "tex.vert").
        shader(Explosion.class, "blur.frag").
        attribute("a_Position").
        attribute("a_TexCoord").
        uniform("u_MVPMatrix").
        uniform("u_Texture");


    public final VertexBufferObject.Definition VBO = new VertexBufferObject.Definition(
            OpenGL.GL_ARRAY_BUFFER, OpenGL.GL_STATIC_DRAW);
    public static final VertexBufferLayout LAYOUT_P2T1 = VertexBufferLayout.create().floats(2).floats(1).build();

    public static final ShaderProgram.Definition PARTICLE_PROGRAM = new ShaderProgram.Definition().
            shader(Explosion.class, "explosion.vert").
            shader(Explosion.class, "explosion.frag").
            attribute("a_Destination").
            attribute("a_TimeToLive").
            uniform("u_Position").
            uniform("u_PointSize").
            uniform("u_Time");

    private final float x, y, vx, vy, r;
    private float time = 0;
    private float pointSize, startX, startY;
    private float s, t;


    public Explosion(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.vx = vx / 2;
        this.vy = vy / 2;
        this.r = RADIUS;
    }

    @Override
    public void onUpdate(float seconds) {
        time += seconds;
        if (time > DURATION) {
            time = 0;
            //kill();
        }
    }

    public float getSpreadRadius() {
        return r * 2;
    }

    private int computeParticleCount(float length) {
        final float area = FloatMathUtils.PI*r*r + length*2*r;
        final float density = 15;
        return (int)FloatMathUtils.ceil(area * density);
    }

    @Override
    public void onInit(DrawContext2f context) {

        final VertexBuffer vb = context.getSharedBuffer();

        final float mLength = FloatMathUtils.length(this.vx, this.vy);

        final float meterHeight = 2 * getSpreadRadius();
        final float meterWidth = meterHeight + mLength;

        final Texture texture = context.obtain(TEXTURE);

        final float meterScale = FloatMathUtils.min((float)texture.getWidth() / meterWidth, // meterWidth * METER_SCALE
                                                    (float)texture.getHeight() / meterHeight);
        final float tWidth = texture.getWidth() / meterScale, tHeight = texture.getHeight() / meterScale;
        final float xScale = 2 / tWidth, yScale = 2 / tHeight, xOff = -1, yOff = -1;

        //final float l = 0, t = 0, r = meterWidth * xScale + xOff, b = meterHeight * yScale + yOff;

        this.s = meterWidth * xScale + xOff;
        this.t = meterHeight * yScale + yOff;
        this.pointSize = PARTICLE_SIZE * meterScale;

        final float sLength = mLength * xScale;
        this.startX = getSpreadRadius() * xScale + xOff;
        this.startY = getSpreadRadius() * yScale + yOff;

        final int particleCount = computeParticleCount(mLength);

        vb.reset(LAYOUT_P2T1, particleCount);
        for (int i=0; i<particleCount; i++) {
            final float v = i < particleCount/2 ? getSpreadRadius() : r;
            final float vx = QRand.RAND.nextFloatBetween(-1, 1) * v,
                        vy = FloatMathUtils.sqrt(v * v - vx * vx) * QRand.RAND.nextFloatBetween(-1, 1);

//            final float a = QRand.RAND.nextFloatBetween(0, FloatMathUtils.TWO_PI);
//            final float vx = FloatMathUtils.cos(a) * getSpreadRadius(), vy = FloatMathUtils.sin(a) * getSpreadRadius();

            final float ttl = QRand.RAND.nextFloatBetween(0.2f, 1f);
            final float x = startX + sLength * QRand.RAND.nextFloat() + vx * xScale,
                        y = startY + vy * yScale;
            vb.put(x, y).put(ttl);
        }

        context.upload(VBO, vb);
    }

    @Override
    public void onDispose(DrawContext2f context) {
        context.delete(VBO);
    }

    @Override
    public void onDraw(DrawContext2f context) {

        context.drawTo(FRAMEBUF);
        context.color(INVISIBLE_BLACK).clear();
        context.use(PARTICLE_PROGRAM).
                attrs(VBO).
                uniform(startX, startY).
                uniform(pointSize).
                uniform(time / DURATION).
                draw(OpenGL.GL_POINTS);
        context.drawToScreen();

        final float tx = TEXTURE_WIDTH*2, ty = TEXTURE_HEIGHT*2;

        context.drawImage(0, ty, tx, ty+ty, TEXTURE);

//
        final VertexBuffer vb = context.getSharedBuffer();
        vb.reset(DefaultShaders.LAYOUT_P2T2).
                put(0, 0).put(0,0).
                put(0, ty).put(0,1).
                put(tx, ty).put(1,1).
                put(tx, 0).put(1,0);
        context.use(BLUR_PROGRAM).
                attrs(vb).
                uniform(context.getActiveMatrix()).
                uniform(TEXTURE).
                draw(OpenGL.GL_TRIANGLE_FAN);

        context.color(Color.WHITE).drawRect(0, 0, tx, ty);
        context.color(Color.WHITE).drawRect(0, ty, tx, ty+ty);

    }
}
