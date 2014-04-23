package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2ID;
import ru.spacearena.engine.graphics.*;
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

    public static final float DURATION = 1f;
    public static final float RADIUS = 2f;

    public static final Texture.Definition TEXTURE = new Texture.Definition().
            wrapS(OpenGL.GL_CLAMP_TO_EDGE).
            wrapT(OpenGL.GL_CLAMP_TO_EDGE).
            empty(256, 256, OpenGL.GL_RGBA, OpenGL.GL_UNSIGNED_BYTE);
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
            uniform("u_MVPMatrix").
            uniform("u_Position").
            uniform("u_Time");

    private final float x, y, vx, vy, r;
    private float time = 0;

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

    @Override
    public void onInit(DrawContext2f context) {
        final VertexBuffer vb = context.getSharedBuffer();

        final float l = FloatMathUtils.length(vx, vy);
        final float s = FloatMathUtils.PI * r*r + l*2*r;
        final float density = 15;
        final int particleCount = (int)FloatMathUtils.ceil(s * density);

        vb.reset(LAYOUT_P2T1, particleCount);
        for (int i=0; i<particleCount; i++) {
            final float d = QRand.RAND.nextFloat();
            final float dx = vx * d, dy = vy * d;

            final float v = i < particleCount/2 ? r+5f : r;
            final float rx = QRand.RAND.nextFloatBetween(-1, 1) * v,
                        ry = FloatMathUtils.sqrt(v * v - rx * rx) * QRand.RAND.nextFloatBetween(-1, 1);
            final float ttl = QRand.RAND.nextFloatBetween(0.2f, 1f);
            vb.put(x + dx + rx, y + dy + ry).put(ttl);
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
        context.getViewport(RECT);
        context.setViewport(0,0,256,256);
        context.color(INVISIBLE_BLACK).clear();
        context.use(PARTICLE_PROGRAM).
                attrs(VBO).
                uniform(context.getActiveMatrix()).
                uniform(x, y).
                uniform(time / DURATION).
                draw(OpenGL.GL_POINTS);
        context.drawTo(null);
        context.setViewport(RECT);

        final VertexBuffer vb = context.getSharedBuffer();
        final Texture texture = context.get(TEXTURE);
        vb.reset(DefaultShaders.LAYOUT_P2T2).
                put(-1, -1).put(texture.getLeft(), texture.getTop()).
                put(-1, 1).put(texture.getLeft(), texture.getBottom()).
                put(1, 1).put(texture.getRight(), texture.getBottom()).
                put(1, -1).put(texture.getRight(), texture.getTop());
        context.use(BLUR_PROGRAM).
                attrs(vb).
                uniform(Matrix.IDENTITY).
                uniform(TEXTURE).
                draw(OpenGL.GL_TRIANGLE_FAN);

    }
}
